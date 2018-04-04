package com.to8to.easydel_compiler;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.to8to.easydel_annotation.Adapter;
import com.to8to.easydel_annotation.AdapterLayout;
import com.to8to.easydel_annotation.Find;
import com.to8to.easydel_annotation.ItemData;
import com.to8to.easydel_annotation.LayoutType;
import com.to8to.easydel_annotation.OnClick;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;

/**
 * Created by same.li on 2018/4/3.
 */
@AutoService(Processor.class)
public class EasyDelProcesser extends AbstractProcessor {

    private Filer mFiler;
    private Elements mElementUtils;
    private Messager mMessager;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        mFiler = processingEnv.getFiler();
        mElementUtils = processingEnv.getElementUtils();
        mMessager = processingEnv.getMessager();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> types = new LinkedHashSet<>();
        types.add(OnClick.class.getCanonicalName());
        types.add(Find.class.getCanonicalName());
        types.add(AdapterLayout.class.getCanonicalName());
        types.add(LayoutType.class.getCanonicalName());
        types.add(Adapter.class.getCanonicalName());
        return types;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }


    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnv) {

        Set<? extends Element> elementsAnnotatedWith = roundEnv.getElementsAnnotatedWith(Adapter.class);

        for (Element element : elementsAnnotatedWith) {
            Adapter annotation = element.getAnnotation(Adapter.class);
            String adapterName  = annotation.className();
            final String filedClassName = element.getSimpleName().toString();
             if(null == adapterName|| adapterName.isEmpty())
             {
                 adapterName = filedClassName;
             }


            String parent = annotation.extend();

             VariableElement typeElement = (VariableElement) element;


            if(null == parent|| parent.isEmpty())
            {
                parent = ClassName.get(typeElement.asType()).toString();
            }

            info("parent=%s", parent);

            final String itemListDataName = "itemListData";



            // public int getItemCount() {return this.itemListData.size();}
            MethodSpec.Builder getItemCount  =  MethodSpec.methodBuilder("getItemCount")
                    .addModifiers(Modifier.PUBLIC)
                    .returns(int.class)
                    .addAnnotation(Override.class)
                    .addStatement("return itemListData.size()");

            MethodSpec.Builder getItemViewType  =  MethodSpec.methodBuilder("getItemViewType")
                    .addModifiers(Modifier.PUBLIC)
                    .addParameter(int.class, "position")
                    .returns(int.class)
                    .addAnnotation(Override.class)
                    .addStatement("return itemListData.get(position).type");


            //public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {}
            ClassName viewHolderClassName =   ClassName.get(AndroidClass.RecyclerView, AndroidClass.ViewHolder);
            MethodSpec.Builder onBindViewHolder  =
                    MethodSpec.methodBuilder("onBindViewHolder")
                    .addModifiers(Modifier.PUBLIC)
                            .addAnnotation(Override.class)
                    .addParameter(viewHolderClassName,
                            "holder")
                     .addParameter(int.class,
                                    "position")
                    .returns(TypeName.VOID);



            //    @Overridepublic RecyclerView.ViewHolder onCreateViewHolder
            //    (ViewGroup parent, int viewType) {return null;}
            MethodSpec.Builder onCreateViewHolder  =
                    MethodSpec.methodBuilder("onCreateViewHolder")
                            .addModifiers(Modifier.PUBLIC)
                            .addAnnotation(Override.class)
                            .addParameter(ClassName.get(AndroidClass.PACKAGE_VIEW,
                                    "ViewGroup"),
                                    "parent")
                            .addParameter(int.class,
                                    "viewType")
                            .returns(viewHolderClassName);


            String[] holders = annotation.holders();
           // HashMap<String, AdapterLayout> layoutHashMap = new HashMap<>();
            int i = 0;
            for (String holder : holders) {
                final String[] packagAnName = ClassUtil.getPackagAnName(holder);
                Set<? extends Element> annotatedWith = roundEnv.getElementsAnnotatedWith(AdapterLayout.class);
                final int layoutSize = annotatedWith.size();
                if(null !=  annotatedWith && layoutSize>0)
                {
                    for (Element layoutElement : annotatedWith) {

                        String layoutHostName = layoutElement.toString();

                        if(holder.equals(layoutHostName))
                        {
                            i++;
                            info("layoutHostName=%s", layoutHostName);
                            //layoutHashMap.put(holder, layoutElement.getAnnotation(AdapterLayout.class));
                            final int temp = i -1;
                            info("temp=%d", temp);
                            AdapterLayout adapterLayoutAnn = layoutElement.getAnnotation(AdapterLayout.class);
                            int id = adapterLayoutAnn.id();
                            int viewType = adapterLayoutAnn.viewType();
                            if(temp == 0)
                            {
                                onCreateViewHolder.beginControlFlow("if($L == viewType)",viewType );

                                onBindViewHolder.beginControlFlow("if (holder instanceof $L)", holder);
                            }
                            else
                            {
                                onCreateViewHolder.beginControlFlow("else if($L == viewType)",viewType );

                                onBindViewHolder.beginControlFlow("else if (holder instanceof $L)", holder);
                            }

                            onCreateViewHolder.addStatement("android.view.View layoutView = android.view.LayoutInflater.from(parent.getContext()).inflate($L, parent, false)", id);

                            onCreateViewHolder.addStatement("$L holder =new $L(layoutView)", holder, holder);

                            List<? extends Element> allMembers = mElementUtils.getAllMembers((TypeElement) layoutElement);
                            if(null != allMembers)
                            {
                                for (Element allMember : allMembers) {
                                    final Find findAnn = allMember.getAnnotation(Find.class);
                                    if(null != findAnn)
                                    {
                                        final int value = findAnn.value();
                                        final String findFieldName = allMember.getSimpleName().toString();
                                        onCreateViewHolder.addStatement("holder.$L = layoutView.findViewById($L)",findFieldName
                                                , value);
                                    }
                                }
                            }
                            onCreateViewHolder.addStatement("return holder");
                            onCreateViewHolder.endControlFlow();

                            onBindViewHolder.addStatement("$L tHolder = ($L)holder ",holder,holder);
                            onBindViewHolder.addStatement("tHolder.update(position, itemListData.get(position))");
                            onBindViewHolder.endControlFlow();
                        }

                    }
                }

            }

            onCreateViewHolder.addStatement("return null");


            FieldSpec itemListDataField =  FieldSpec.builder(
                    //List<ItemData> itemListData
                    ParameterizedTypeName.get(ArrayList.class,ItemData.class),
                    itemListDataName,
                     Modifier.PUBLIC,Modifier.FINAL)
                    //itemListData = new ArrayList<ItemData>();
                    .initializer(CodeBlock.of("new $T()", ParameterizedTypeName.get(ArrayList.class,
                            ItemData.class)))
                    .build();
            //构成适配器的名字xx$$innerTypeName
            String innerTypeName = adapterName.substring(0, 1).toUpperCase();
            if(adapterName.length()>1)
            {
                innerTypeName += adapterName.substring(1, adapterName.length());
            }
            info(" innerTypeName  :%s ",innerTypeName);

            TypeSpec apdater = null;

            try {

                apdater = TypeSpec.classBuilder(element.getEnclosingElement().getSimpleName()+ innerTypeName)
                        .addModifiers(Modifier.PUBLIC)
                        .superclass(ClassName.get(element.asType()))
                        .addField(itemListDataField)
                        .addMethod(getItemCount.build())
                        .addMethod(getItemViewType.build())
                        .addMethod(onBindViewHolder.build())
                        .addMethod(onCreateViewHolder.build())
                        .build();
            } catch (Exception e) {
                info("Exception=%s", e.toString());
            }
            final String packagename =  mElementUtils.getPackageOf(element).getQualifiedName().toString();
            info("packagename=%s", packagename);
           // JavaFileFixed javaFile = JavaFileFixed.builder(PACKAGE, typeSpec).addType(parent).build();
            JavaFile javaFile = JavaFile.builder(packagename
                    , apdater)
                    .build();
            try {
                javaFile.writeTo(mFiler);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return true;
    }


    private void error(String msg, Object... args) {
        mMessager.printMessage(Diagnostic.Kind.ERROR, String.format(msg, args));
    }

    private void info(String msg, Object... args) {
        mMessager.printMessage(Diagnostic.Kind.NOTE, String.format(msg, args));
    }
}
