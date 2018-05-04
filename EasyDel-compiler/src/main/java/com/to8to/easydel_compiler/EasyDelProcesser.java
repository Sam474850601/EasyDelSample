package com.to8to.easydel_compiler;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.to8to.easydel_annotation.Adapter;
import com.to8to.easydel_annotation.AdapterLayout;
import com.to8to.easydel_annotation.ContainerType;
import com.to8to.easydel_annotation.Find;
import com.to8to.easydel_annotation.HelperName;
import com.to8to.easydel_annotation.Null;
import com.to8to.easydel_annotation.OnChildItemClick;
import com.to8to.easydel_annotation.OnClick;
import com.to8to.easydel_annotation.ViewLayout;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
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
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.MirroredTypeException;
import javax.lang.model.type.MirroredTypesException;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.swing.text.View;
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
        types.add(OnChildItemClick.class.getCanonicalName());
        types.add(Find.class.getCanonicalName());
        types.add(AdapterLayout.class.getCanonicalName());
        types.add(Adapter.class.getCanonicalName());
        types.add(ViewLayout.class.getCanonicalName());
        types.add(OnClick.class.getCanonicalName());
        types.add(ContainerType.class.getCanonicalName());
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

    private final HashMap<String , Element> rList = new HashMap<>();


    //R.id.xx
    public static final int IDTYPE_FIELD =  1;

    //R.layout.xx
    public static final int IDTYPE_LAYOUT =  2;




    private String getViewIdKey(int id, int idType)
    {
        Set<Map.Entry<String, Element>> entries = rList.entrySet();
        for (Map.Entry<String, Element> entry : entries) {
            final String rClassName = entry.getKey();
            final String pk = rClassName.substring(0, rClassName.lastIndexOf("."));
            String idTypestr = null;
            switch (idType)
            {
                case IDTYPE_FIELD:
                {
                    idTypestr = "id";
                }
                break ;

                case IDTYPE_LAYOUT:
                {
                    idTypestr = "layout";
                }
                break ;

                default:
                    idTypestr = "id";
            }

            TypeElement typeElement = mElementUtils.getTypeElement(pk + ".MR."+idTypestr);
            if(null == typeElement)
            {
                typeElement = mElementUtils.getTypeElement(rClassName+"."+idTypestr);
            }

            if(null == typeElement)
                continue;
            List<? extends Element> allMembers = mElementUtils.getAllMembers(typeElement);
            for (Element member : allMembers) {
                if (member.getKind().isField()) {

                    String fieldName = member.toString();

                    if(member instanceof  VariableElement)
                    {
                        VariableElement variableElement = (VariableElement) member;
                        Object constantValue = variableElement.getConstantValue();

                        if (null != constantValue) {
                            if(id == (int)constantValue)
                            {
                                String format = String.format("@host.getResources().getIdentifier(\"%s\", \""+idTypestr+"\", @package)",
                                        fieldName);
                                return format;
                            }
                        }

                    }

                }
            }

        }
        return id+"";
    }

    private void _createCodeForRecyclerViewAdapter(RoundEnvironment roundEnv) {
        Set<? extends Element> rootElements = roundEnv.getRootElements();

        for (Element resElement : rootElements) {
            String s = resElement.toString();
            if(!s.startsWith("android") && s.endsWith(".R"))
            {
                rList.put(s, resElement);
            }
        }

        for (Element element : rootElements) {

            final String packagename = mElementUtils.getPackageOf(element).getQualifiedName().toString();
            final String hostName = element.getSimpleName().toString();
            List<? extends Element> allMembers = mElementUtils.getAllMembers((TypeElement) element);
            if (null == allMembers)
                continue;
            final String hostclassName = ClassName.get(element.asType()).toString();
            final String hostInstancename = "host";
            MethodSpec.Builder inject= _inject(hostclassName, hostInstancename);

            MethodSpec.Builder loadLayoutView = null;

            boolean hasInjectAnnotation =false;
            final   HashMap<Integer, String> findFields= new HashMap<>();
            final   HashMap<Integer, String> onClikeMethods= new HashMap<>();
            ContainerType containerType =   element.getAnnotation(ContainerType.class);
            String packageName = "";
            String findViewInstance  = null;
            String getResourceInstance  = null;
            int type = ContainerType.TYPE_ADAPTER;
            if(null != containerType)
            {
                type = containerType.value();
            }
            if(ContainerType.TYPE_ACTIVITY == type)
            {
                findViewInstance = "host";
                getResourceInstance = "host";
                packageName = "host.getPackageName()";
            }
            else if(ContainerType.TYPE_FRAGMENT == type)
            {
                findViewInstance = "host.getView()";
                getResourceInstance = findViewInstance+".getContext()";
                packageName = "host.getContext().getPackageName()";
            }





            for (Element member : allMembers) {
                final String fieldClassName = member.getSimpleName().toString();
                Adapter annotation = member.getAnnotation(Adapter.class);
                if(null != annotation)
                {
                    hasInjectAnnotation = true;
                    String adapterName = annotation.className();


                    if (null == adapterName || adapterName.isEmpty()) {
                        adapterName = fieldClassName;
                    }
                    String parent = null;
                    try {
                        Class value = annotation.extendsClass();
                        if (!Null.class.isAssignableFrom(value)) {
                            parent = value.getName();
                        }

                    } catch (MirroredTypeException mte) {
                        TypeMirror typeMirror = mte.getTypeMirror();
                        DeclaredType classTypeMirror = (DeclaredType) typeMirror;
                        TypeElement classTypeElement = (TypeElement) classTypeMirror.asElement();
                        final String className = classTypeElement.getQualifiedName().toString();
                        if (!Null.class.getName().equals(className)) {
                            parent = className;
                        }
                    }
                    VariableElement typeElement = (VariableElement) member;

                    if (null == parent || parent.isEmpty()) {
                        parent = ClassName.get(typeElement.asType()).toString();
                    }
                    ClassName viewHolderClassName = ClassName.get(AndroidClass.RecyclerView, AndroidClass.ViewHolder);
                    MethodSpec.Builder onBindViewHolder = _onBindViewHolder(viewHolderClassName);
                    MethodSpec.Builder onCreateViewHolder = _onCreateViewHolder(viewHolderClassName);


                    _addCodeForOnBindViewHolder(annotation, roundEnv, onCreateViewHolder, onBindViewHolder);
                    String innerTypeName = adapterName.substring(0, 1).toUpperCase();
                    if (adapterName.length() > 1) {
                        innerTypeName += adapterName.substring(1, adapterName.length());
                    }



                    TypeSpec apdater = null;
                    try {
                        String[] packagAnName = ClassUtil.getPackagAnName(parent);
                        final String newAdapterClassName = hostName + "$$" + innerTypeName;
                        System.out.println("adapter:"+packagename+"."+newAdapterClassName);
                        inject.addStatement("host.$L= new $L()", fieldClassName, packagename + "." + newAdapterClassName);
                        apdater = TypeSpec.classBuilder(newAdapterClassName)
                                .addModifiers(Modifier.PUBLIC)
                                .superclass(ClassName.get(packagAnName[0], packagAnName[1]))
                                //.addField(itemListDataField)
                                .addMethod(onBindViewHolder.build())
                                .addMethod(onCreateViewHolder.build())
                                .build();

                    } catch (Exception e) {
                        info("Exception=%s", e.toString());
                    }
                    // JavaFileFixed javaFile = JavaFileFixed.builder(PACKAGE, typeSpec).addType(parent).build();

                    try {
                        JavaFile.builder(packagename
                                , apdater)
                                .build().writeTo(mFiler);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

               Find find = member.getAnnotation(Find.class);
               if(null != find && null != findViewInstance)
               {
                   final int viewId = find.value();
                   findFields.put(viewId,fieldClassName);
                   hasInjectAnnotation = true;
                   inject.addStatement("host.$L = ($L)$L.findViewById($L)", fieldClassName,member.asType().toString(),
                           findViewInstance,
                           getViewIdKey(find.value(), IDTYPE_FIELD)
                                   .replace("@package", packageName)
                                   .replace("@host", getResourceInstance)
                                    );
               }
                OnClick onClick = member.getAnnotation(OnClick.class);
                if(null != onClick && null != findViewInstance)
                {
                    hasInjectAnnotation = true;
                    onClikeMethods.put(onClick.value(), member.getSimpleName().toString());
                }
            }

            Set<Map.Entry<Integer, String>> entries = onClikeMethods.entrySet();
            for (Map.Entry<Integer, String> entry : entries) {
                final   String fieldName = findFields.get(entry.getKey());
                final String method = entry.getValue();
                if(null == fieldName)
                {
                    inject.addStatement("$L.findViewById($L).setOnClickListener(new android.view.View.OnClickListener() {@Override public void onClick(android.view.View view) {host.$L(view);}})",
                            findViewInstance ,

                            getViewIdKey(entry.getKey(),IDTYPE_FIELD)
                                    .replace("@host", getResourceInstance)
                                    .replace("@package", packageName)
                            , method );
                }
                else
                {

                    inject.addStatement("host.$L.setOnClickListener(new android.view.View.OnClickListener() {@Override public void onClick(android.view.View view) {host.$L(view);}})", fieldName ,method );
                }
            }

            ViewLayout viewLayout = element.getAnnotation(ViewLayout.class);

            if(null != viewLayout)
            {
                loadLayoutView = _loadLayoutView(hostclassName, hostInstancename);
                String viewIdKey = getViewIdKey(viewLayout.value(), IDTYPE_LAYOUT);
                viewIdKey = viewIdKey
                        .replace("@host", getResourceInstance)
                        .replace("@package", packageName);

                loadLayoutView.addStatement("return android.view.LayoutInflater.from($L).inflate($L, null, false)",
                         getResourceInstance,
                         viewIdKey
                );
               hasInjectAnnotation = true;
            }

            if(hasInjectAnnotation)
            {
                TypeSpec.Builder injectHelper = TypeSpec.classBuilder(hostName + "$$" + HelperName.NAME_SIMPLECLASS_ADAPTER_HELPER)
                        .addMethod(inject.build());

                if (null != loadLayoutView) {
                    injectHelper.addMethod(loadLayoutView.build());
                }

                try
                {
                    JavaFile.builder(packagename
                            , injectHelper.build())
                            .build().writeTo(mFiler);
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }



    private String  getId(int id)
    {


        return "";
    }

    private void _addCodeForOnBindViewHolder(Adapter annotation, RoundEnvironment roundEnv,
                                             MethodSpec.Builder onCreateViewHolder, MethodSpec.Builder onBindViewHolder) {

        String[] holders = null;

        // HashMap<String, AdapterLayout> layoutHashMap = new HashMap<>();
        try {
            Class[] value = annotation.value();
            if (null == value || 0 == value.length)
                return;
            holders = new String[value.length];
            for (int i = 0; i < value.length; i++) {
                holders[i] = value[i].getName();
            }

        } catch (MirroredTypesException mte) {
            System.out.println("MirroredTypesException:" + mte.toString());
            List<? extends TypeMirror> typeArguments = mte.getTypeMirrors();
            if (null == typeArguments || typeArguments.isEmpty())
                return;
            final int size = typeArguments.size();
            holders = new String[size];
            for (int i = 0; i < size; i++) {
                DeclaredType classTypeMirror = (DeclaredType) typeArguments.get(i);
                TypeElement classTypeElement = (TypeElement) classTypeMirror.asElement();
                holders[i] = classTypeElement.getQualifiedName().toString().toString();
            }
        }

        if (null == holders)
            return;

        int i = 0;
        for (String holder : holders) {

            TypeElement layoutElement = mElementUtils.getTypeElement(holder);
            if (null != layoutElement) {
                i++;
                //layoutHashMap.put(holder, layoutElement.getAnnotation(AdapterLayout.class));
                final int temp = i - 1;
                AdapterLayout adapterLayoutAnn = layoutElement.getAnnotation(AdapterLayout.class);
                final int layoutId = adapterLayoutAnn.id();
                final int viewType = adapterLayoutAnn.viewType();
                String viewModel = null;

                try {
                    Class<?> viewModelClass = adapterLayoutAnn.itemModel();
                    if (!Null.class.isAssignableFrom(viewModelClass)) {
                        viewModel = viewModelClass.getName();
                    }
                } catch (MirroredTypeException mte) {
                    TypeMirror typeMirror = mte.getTypeMirror();
                    DeclaredType classTypeMirror = (DeclaredType) typeMirror;
                    TypeElement classTypeElement = (TypeElement) classTypeMirror.asElement();
                    final String className = classTypeElement.getQualifiedName().toString();
                    if (!Null.class.getName().equals(className)) {
                        viewModel = className;
                    }
                }
                if (temp == 0) {
                    onCreateViewHolder.beginControlFlow("if($L == viewType)", viewType);

                    onBindViewHolder.beginControlFlow("if (holder instanceof $L)", holder);
                } else {
                    onCreateViewHolder.beginControlFlow("else if($L == viewType)", viewType);

                    onBindViewHolder.beginControlFlow("else if (holder instanceof $L)", holder);
                }

                onCreateViewHolder.addStatement("final android.view.View layoutView = android.view.LayoutInflater.from(parent.getContext()).inflate($L, parent, false)",
                        getViewIdKey(layoutId, IDTYPE_LAYOUT)
                                .replace("@host", "parent.getContext()")
                                .replace("@package",
                                "parent.getContext().getPackageName()")
                                );

                onCreateViewHolder.addStatement("final $L holder =new $L(layoutView)", holder, holder);


                onBindViewHolder.addStatement("final $L tHolder = ($L)holder ", holder, holder);
                if (null == viewModel) {
                    onBindViewHolder.addStatement("tHolder.update(position, getItemData(position))");
                } else {
                    onBindViewHolder.addStatement("tHolder.update(position, ($L)getItemData(position))", viewModel);
                }
                final HashMap<Integer, String> viewIds = new HashMap<>();
               final  HashMap<Integer, String > onChildClicks = new HashMap<>();
                List<? extends Element> allMembers = mElementUtils.getAllMembers(layoutElement);
                if (null != allMembers) {
                    for (Element member : allMembers) {
                        final Find findAnn = member.getAnnotation(Find.class);

                        if (null != findAnn) {
                            final int viewId = findAnn.value();
                            final String findFieldName = member.getSimpleName().toString();
                            onCreateViewHolder.addStatement("holder.$L = ($L)layoutView.findViewById($L)", findFieldName
                                    , ClassName.get(member.asType()).toString(),
                                             getViewIdKey(viewId, IDTYPE_FIELD)
                                            .replace("@host", "parent.getContext()")
                                            .replace("@package", "parent.getContext().getPackageName()"
                                            )


                            );
                            viewIds.put(viewId, findFieldName);
                        }

                        final OnChildItemClick onChildItemClick = member.getAnnotation(OnChildItemClick.class);
                        if (null != onChildItemClick) {
                            final int viewId = onChildItemClick.value();
                            onChildClicks.put(viewId, member.getSimpleName().toString());
                        }

                        String memberName = member.toString();

                        if (memberName.contains("onItemClick") && memberName.contains("int") && memberName.contains(AndroidClass.VIEW)) {
                            if (null == viewModel) {
                                onBindViewHolder.addStatement("tHolder.itemView.setOnClickListener(new $L.OnClickListener(){@Override public void onClick($L view) {tHolder.onItemClick(position, getItemData(position), view);}})"
                                        , AndroidClass.VIEW, AndroidClass.VIEW);
                            } else {
                                onBindViewHolder.addStatement("tHolder.itemView.setOnClickListener(new $L.OnClickListener(){@Override public void onClick($L view) {tHolder.onItemClick(position, ($L)getItemData(position), view);}})"
                                        , AndroidClass.VIEW, AndroidClass.VIEW, viewModel);
                            }
                        }

                        if (memberName.contains("onItemLongClick") && memberName.contains("int") && memberName.contains(AndroidClass.VIEW)) {
                            if (null == viewModel) {
                                onBindViewHolder.addStatement("tHolder.itemView.setOnLongClickListener(new $L.OnLongClickListener(){@Override public boolean onLongClick($L view) { return tHolder.onItemLongClick(position, getItemData(position), view);}})"
                                        , AndroidClass.VIEW, AndroidClass.VIEW);
                            } else {
                                onBindViewHolder.addStatement("tHolder.itemView.setOnLongClickListener(new $L.OnLongClickListener(){@Override public boolean onLongClick($L view) { return tHolder.onItemLongClick(position, ($L)getItemData(position), view);}})"
                                        , AndroidClass.VIEW, AndroidClass.VIEW, viewModel);
                            }

                        }
                    }
                    Set<Map.Entry<Integer, String>> entries = onChildClicks.entrySet();
                    for (Map.Entry<Integer, String> entry : entries) {
                        int viewId = entry.getKey();
                        _addOnClickCode(viewModel, onBindViewHolder, viewId, viewIds.get(viewId), entry.getValue());
                    }
                }
                onCreateViewHolder.addStatement("return holder");
                onCreateViewHolder.endControlFlow();


                onBindViewHolder.endControlFlow();
            }


        }

        onCreateViewHolder.addStatement("return null");

    }

    private void _addOnClickCode(String viewModel, MethodSpec.Builder onBindViewHolder, Integer viewId, String fieldName,  String method ) {

        if (null == viewModel) {


            if(null == fieldName)
            {


                onBindViewHolder.addStatement("tHolder.itemView.findViewById($L).setOnClickListener(new $L.OnClickListener()" +
                                "{@Override public void onClick($L view) {tHolder.$L(position,  getItemData(position), view);}})",
                        getViewIdKey(viewId, IDTYPE_FIELD)
                                .replace("@package",
                                "holder.itemView.getContext().getPackageName()")
                                .replace("@host", "holder.itemView.getContext()"),
                        AndroidClass.VIEW, AndroidClass.VIEW, method);
            }
            else
            {
                onBindViewHolder.addStatement("tHolder.$L.setOnClickListener(new $L.OnClickListener()" +
                                "{@Override public void onClick($L view) {tHolder.$L(position,  getItemData(position), view);}})",
                        fieldName, AndroidClass.VIEW, AndroidClass.VIEW, method);
            }

        } else {
            if(null == fieldName)
            {
                onBindViewHolder.addStatement("tHolder.itemView.findViewById($L).setOnClickListener(new $L.OnClickListener()" +
                                "{@Override public void onClick($L view) {tHolder.$L(position,  ($L)getItemData(position), view);}})",
                        getViewIdKey(viewId, IDTYPE_FIELD)
                                .replace("@package",
                                "holder.itemView.getContext().getPackageName()")
                                .replace("@host", "holder.itemView.getContext()")
                        , AndroidClass.VIEW, AndroidClass.VIEW, method, viewModel);
            }
            else
            {
                onBindViewHolder.addStatement("tHolder.$L.setOnClickListener(new $L.OnClickListener()" +
                                "{@Override public void onClick($L view) {tHolder.$L(position,  ($L)getItemData(position), view);}})",
                        fieldName, AndroidClass.VIEW, AndroidClass.VIEW, method, viewModel);
            }

        }

    }

    private void _addOnClickCodeById(String viewModel, MethodSpec.Builder onBindViewHolder, String findFieldName, int id, List<? extends Element> allMembers) {

        for (Element member : allMembers) {
            final OnChildItemClick onChildItemClick = member.getAnnotation(OnChildItemClick.class);
            if (null != onChildItemClick) {
                final int tId = onChildItemClick.value();
                if (tId == id) {
                    if (null == viewModel) {
                        onBindViewHolder.addStatement("tHolder.$L.setOnClickListener(new $L.OnClickListener() {@Override public void onClick($L view) " +
                                        "{tHolder.$L(position,  getItemData(position), view);}})",
                                findFieldName, AndroidClass.VIEW, AndroidClass.VIEW, member.getSimpleName().toString());
                    } else {
                        onBindViewHolder.addStatement("tHolder.$L.setOnClickListener(new $L.OnClickListener() {@Override public void onClick($L view) " +
                                        "{tHolder.$L(position,  ($L)getItemData(position), view);}})"
                                , findFieldName, AndroidClass.VIEW, AndroidClass.VIEW, member.getSimpleName().toString(), viewModel);
                    }
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

    private MethodSpec.Builder _inject(String hostClassName, String hostInstanceName) {
        String[] packagAnName = ClassUtil.getPackagAnName(hostClassName);

        return
                MethodSpec.methodBuilder(HelperName.NAME_METHOD_INJECT)
                        .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                        .addParameter(ClassName.get(packagAnName[0], packagAnName[1]), hostInstanceName, Modifier.FINAL)
                        .returns(TypeName.VOID);


    }

    private MethodSpec.Builder _loadLayoutView(String hostClassName, String hostInstanceName) {
        String[] packagAnName = ClassUtil.getPackagAnName(hostClassName);

        return
                MethodSpec.methodBuilder(HelperName.NAME_METHOD_LOAD_LAYOUTVIEW)
                        .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                        .addParameter(ClassName.get(packagAnName[0], packagAnName[1]), hostInstanceName, Modifier.FINAL)
                        .returns(ClassName.get(AndroidClass.PACKAGE_VIEW,"View"));


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
