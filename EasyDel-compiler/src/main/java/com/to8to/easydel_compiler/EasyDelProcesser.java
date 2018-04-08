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
import com.to8to.easydel_annotation.ViewLayout;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
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
import javax.lang.model.element.Name;
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
        types.add(ViewLayout.class.getCanonicalName());
        return types;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }


    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnv) {

        _createCodeForRecyclerViewAdapter(roundEnv);

        return true;
    }

    private void _createCodeForRecyclerViewAdapter(RoundEnvironment roundEnv) {
        Set<? extends Element> elementsAnnotatedWith = roundEnv.getElementsAnnotatedWith(Adapter.class);
        for (Element element : elementsAnnotatedWith) {

            Adapter annotation = element.getAnnotation(Adapter.class);


            String adapterName = annotation.className();
            final String filedClassName = element.getSimpleName().toString();
            if (null == adapterName || adapterName.isEmpty()) {
                adapterName = filedClassName;
            }
            String parent = annotation.extend();
            VariableElement typeElement = (VariableElement) element;
            if (null == parent || parent.isEmpty()) {
                parent = ClassName.get(typeElement.asType()).toString();
            }
            info("parent=%s", parent);
            ClassName viewHolderClassName = ClassName.get(AndroidClass.RecyclerView, AndroidClass.ViewHolder);
            MethodSpec.Builder onBindViewHolder = _onBindViewHolder(viewHolderClassName);
            MethodSpec.Builder onCreateViewHolder = _onCreateViewHolder(viewHolderClassName);
            _addCodeForOnBindViewHolder(annotation, roundEnv, onCreateViewHolder, onBindViewHolder);
            String innerTypeName = adapterName.substring(0, 1).toUpperCase();
            if (adapterName.length() > 1) {
                innerTypeName += adapterName.substring(1, adapterName.length());
            }
            info(" innerTypeName  :%s ", innerTypeName);
            TypeSpec apdater = null;
            try {
                String[] packagAnName = ClassUtil.getPackagAnName(parent);

                apdater = TypeSpec.classBuilder(element.getEnclosingElement().getSimpleName() + "$$"+innerTypeName)
                        .addModifiers(Modifier.PUBLIC)
                        .superclass(ClassName.get(packagAnName[0], packagAnName[1]))
                        //.addField(itemListDataField)
                        .addMethod(onBindViewHolder.build())
                        .addMethod(onCreateViewHolder.build())
                        .build();
            } catch (Exception e) {
                info("Exception=%s", e.toString());
            }
            final String packagename = mElementUtils.getPackageOf(element).getQualifiedName().toString();
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
    }

    //通过循环来处理，不保存操作是为了整个项目代码多时候gradle内存溢出。但是增加了编辑耗时
    private void _addCodeForOnBindViewHolder(Adapter annotation, RoundEnvironment roundEnv,
                                             MethodSpec.Builder onCreateViewHolder, MethodSpec.Builder onBindViewHolder) {

        String[] holders = annotation.holders();
        // HashMap<String, AdapterLayout> layoutHashMap = new HashMap<>();
        int i = 0;
        for (String holder : holders) {
            Set<? extends Element> annotatedWith = roundEnv.getElementsAnnotatedWith(AdapterLayout.class);
            final int layoutSize = annotatedWith.size();
            if (null != annotatedWith && layoutSize > 0) {
                for (Element layoutElement : annotatedWith) {

                    final  String layoutHostName = layoutElement.toString();

                    if (holder.equals(layoutHostName)) {
                        i++;
                        info("layoutHostName=%s", layoutHostName);
                        //layoutHashMap.put(holder, layoutElement.getAnnotation(AdapterLayout.class));
                        final int temp = i - 1;
                        info("temp=%d", temp);
                        AdapterLayout adapterLayoutAnn = layoutElement.getAnnotation(AdapterLayout.class);
                        final int layoutId = adapterLayoutAnn.id();
                        final int viewType = adapterLayoutAnn.viewType();
                        if (temp == 0) {
                            onCreateViewHolder.beginControlFlow("if($L == viewType)", viewType);

                            onBindViewHolder.beginControlFlow("if (holder instanceof $L)", holder);
                        } else {
                            onCreateViewHolder.beginControlFlow("else if($L == viewType)", viewType);

                            onBindViewHolder.beginControlFlow("else if (holder instanceof $L)", holder);
                        }

                        onCreateViewHolder.addStatement("final android.view.View layoutView = android.view.LayoutInflater.from(parent.getContext()).inflate($L, parent, false)", layoutId);

                        onCreateViewHolder.addStatement("final $L holder =new $L(layoutView)", holder, holder);


                        onBindViewHolder.addStatement("final $L tHolder = ($L)holder ", holder, holder);
                        onBindViewHolder.addStatement("tHolder.update(position, getItemData(position))");

                        HashMap<Integer, Integer > viewIds  = new HashMap<>();
                        List<? extends Element> allMembers = mElementUtils.getAllMembers((TypeElement) layoutElement);
                        if (null != allMembers) {
                            for (Element member : allMembers) {
                                final Find findAnn = member.getAnnotation(Find.class);

                                if (null != findAnn) {
                                    final int viewId = findAnn.value();
                                    final String findFieldName = member.getSimpleName().toString();
                                    onCreateViewHolder.addStatement("holder.$L = ($L)layoutView.findViewById($L)", findFieldName
                                            , ClassName.get(member.asType()).toString(), viewId);
                                    _addOnClickCodeById(onCreateViewHolder, findFieldName, viewId, allMembers);
                                    viewIds.put(viewId, viewId);
                                }

                                final OnClick onClick = member.getAnnotation(OnClick.class);
                               if(null != onClick)
                               {
                                   final int  viewId = onClick.value();
                                   Integer integer = viewIds.get(viewId);
                                   if(null == integer)
                                   {
                                       _addOnClickCode(onCreateViewHolder, viewId, member);
                                   }
                               }

                                String memberName   = member.toString();

                                if(memberName.contains("onItemClick")&&memberName.contains("int")&&memberName.contains(ItemData.class.getName().toString())&&memberName.contains(AndroidClass.VIEW))
                                {

                                   onBindViewHolder.addStatement("tHolder.itemView.setOnClickListener(new $L.OnClickListener(){@Override public void onClick($L view) {tHolder.onItemClick(position, getItemData(position), view);}})"
                                 , AndroidClass.VIEW, AndroidClass.VIEW);
                                }

                                if(memberName.contains("onItemLongClick")&&memberName.contains("int")&&memberName.contains(ItemData.class.getName().toString())&&memberName.contains(AndroidClass.VIEW))
                                {

                                    onBindViewHolder.addStatement("tHolder.itemView.setOnLongClickListener(new $L.OnLongClickListener(){@Override public boolean onLongClick($L view) { return tHolder.onItemLongClick(position, getItemData(position), view);}})"
                                            , AndroidClass.VIEW, AndroidClass.VIEW);
                                }
                            }
                        }
                        onCreateViewHolder.addStatement("return holder");
                        onCreateViewHolder.endControlFlow();


                        onBindViewHolder.endControlFlow();
                    }

                }
            }

        }

        onCreateViewHolder.addStatement("return null");

    }

    private void _addOnClickCode(MethodSpec.Builder onCreateViewHolder, int viewId, Element member) {
        onCreateViewHolder.addStatement("layoutView.findViewById($L).setOnClickListener(new $L.OnClickListener(){@Override public void onClick($L view) {holder.$L(view);}})",
                viewId,  AndroidClass.VIEW, AndroidClass.VIEW, member.getSimpleName());
    }

    private void _addOnClickCodeById(MethodSpec.Builder onCreateViewHolder,String findFieldName,  int id, List<? extends Element> allMembers) {

        for (Element member : allMembers) {
            final OnClick onClick = member.getAnnotation(OnClick.class);
            if(null != onClick)
            {
                final int tId = onClick.value();
                if(tId == id)
                {
                    onCreateViewHolder.addStatement("holder.$L.setOnClickListener(new $L.OnClickListener() {@Override public void onClick($L view) " +
                                    "{holder.$L(view);}})"
                            , findFieldName, AndroidClass.VIEW, AndroidClass.VIEW, member.getSimpleName().toString());
                }
            }
        }



    }

    private MethodSpec.Builder _onCreateViewHolder(ClassName viewHolderClassName) {
        //    @Overridepublic RecyclerView.ViewHolder onCreateViewHolder
        //    (ViewGroup parent, int viewType) {return null;}
        return
                MethodSpec.methodBuilder("onCreateViewHolder")
                        .addModifiers(Modifier.PUBLIC)
                        .addAnnotation(Override.class)
                        .addParameter(ClassName.get(AndroidClass.PACKAGE_VIEW,
                                "ViewGroup"),
                                "parent")
                        .addParameter(int.class,
                                "viewType")
                        .returns(viewHolderClassName);
    }

    private MethodSpec.Builder _onBindViewHolder(ClassName viewHolderClassName) {

            return MethodSpec.methodBuilder("onBindViewHolder")
                        .addModifiers(Modifier.PUBLIC)
                        .addAnnotation(Override.class)
                        .addParameter(viewHolderClassName,
                                "holder")
                        .addParameter(int.class,
                                "position", Modifier.FINAL)
                        .returns(TypeName.VOID);
    }




    private void error(String msg, Object... args) {
        mMessager.printMessage(Diagnostic.Kind.ERROR, String.format(msg, args));
    }

    private void info(String msg, Object... args) {
        mMessager.printMessage(Diagnostic.Kind.NOTE, String.format(msg, args));
    }
}
