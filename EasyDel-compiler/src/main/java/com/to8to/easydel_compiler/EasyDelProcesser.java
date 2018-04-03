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
import com.to8to.easydel_annotation.Find;
import com.to8to.easydel_annotation.ItemData;
import com.to8to.easydel_annotation.Layout;
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
        types.add(Layout.class.getCanonicalName());
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
            VariableElement variableElement  = (VariableElement) element;
            final  Element hostElemnt =  element.getEnclosingElement();
            final String simpleClassName = hostElemnt.getSimpleName().toString();
            final String  className =   ((TypeElement)hostElemnt).getQualifiedName().toString();
            final String fieldClassName = ClassName.get(element.asType()).toString();
            final String fieldName = variableElement.getSimpleName().toString();

            final String itemListDataName = "itemListData";

            // public int getItemCount() {return this.itemListData.size();}
            MethodSpec.Builder getItemCount  =  MethodSpec.methodBuilder("getItemCount")
                    .addModifiers(Modifier.PUBLIC)
                    .returns(int.class)
                    .addStatement("return itemListData.size()");

            //public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {}
            ClassName viewHolderClassName =   ClassName.get(AndroidClass.RecyclerView, AndroidClass.ViewHolder);
            MethodSpec.Builder onBindViewHolder  =
                    MethodSpec.methodBuilder("onBindViewHolder")
                    .addModifiers(Modifier.PUBLIC)
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
                            .addParameter(ClassName.get(AndroidClass.PACKAGE_VIEW,
                                    "View"),
                                    "parent")
                            .addParameter(int.class,
                                    "viewType")
                            .returns(viewHolderClassName)
                            .addStatement("return null");

            FieldSpec itemListDataField =  FieldSpec.builder(
                    //List<ItemData> itemListData
                    ParameterizedTypeName.get(ArrayList.class,ItemData.class),
                    itemListDataName,
                     Modifier.PRIVATE,Modifier.FINAL)
                    //itemListData = new ArrayList<ItemData>();
                    .initializer(CodeBlock.of("new $T()", ParameterizedTypeName.get(ArrayList.class,
                            ItemData.class)))
                    .build();
            //构成适配器的名字xx$$innerTypeName
            String innerTypeName = fieldName.substring(0, 1).toUpperCase();
            if(fieldName.length()>1)
            {
                innerTypeName +=fieldName.substring(1, fieldName.length());
            }
            TypeSpec apdater = TypeSpec.classBuilder(simpleClassName+"$$"+innerTypeName)
                    .addModifiers(Modifier.PUBLIC)
                    .superclass(ClassName.get(element.asType()))
                    .addField(itemListDataField)
                    .addMethod(getItemCount.build())
                    .addMethod(onBindViewHolder.build())
                    .addMethod(onCreateViewHolder.build())
                    .build();
            final String packagename =  mElementUtils.getPackageOf(element).getQualifiedName().toString();
            info("packagename=%s", packagename);
            JavaFile javaFile = JavaFile.builder(packagename
                    , apdater)
                    .build();
            try {
                javaFile.writeTo(mFiler);
            } catch (IOException e) {
                e.printStackTrace();
            }
            info("variableElement simpleClassName  :%s ",simpleClassName);
            info("variableElement className  :%s ",className);
            info("variableElement fieldClassName :%s ", fieldClassName);
            //field
            info("variableElement fieldName:%s ", fieldName);
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
