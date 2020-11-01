//package com.vero.libnavcompiler;
//
//import com.alibaba.fastjson.JSON;
//import com.alibaba.fastjson.JSONObject;
//import com.google.auto.service.AutoService;
//import com.vero.libnavannotation.ActivityDestination;
//import com.vero.libnavannotation.FragmentDestination;
//
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.OutputStreamWriter;
//import java.lang.annotation.Annotation;
//import java.util.HashMap;
//import java.util.Set;
//
//import javax.annotation.processing.AbstractProcessor;
//import javax.annotation.processing.Filer;
//import javax.annotation.processing.Messager;
//import javax.annotation.processing.ProcessingEnvironment;
//import javax.annotation.processing.Processor;
//import javax.annotation.processing.RoundEnvironment;
//import javax.annotation.processing.SupportedAnnotationTypes;
//import javax.annotation.processing.SupportedSourceVersion;
//import javax.lang.model.SourceVersion;
//import javax.lang.model.element.Element;
//import javax.lang.model.element.TypeElement;
//import javax.tools.Diagnostic;
//import javax.tools.FileObject;
//import javax.tools.StandardLocation;
//
//
///**
// * APP页面导航信息收集注解处理器
// * <p>
// * AutoService注解：就这么一标记，annotationProcessor  project()应用一下,编译时就能自动执行该类了。
// * <p>
// * SupportedSourceVersion注解:声明我们所支持的jdk版本
// * <p>
// * SupportedAnnotationTypes:声明该注解处理器想要处理那些注解
// */
//
//@AutoService(Processor.class)
//@SupportedSourceVersion(SourceVersion.RELEASE_8)
//@SupportedAnnotationTypes({"com.vero.libnavannotation.ActivityDestination", "com.vero.libnavannotation.FragmentDestination"})
//public class NavProcessor2 extends AbstractProcessor {
//    private Messager messager;
//    private Filer filer;
//    private static final String OUT_PUT_FILE_NAME = "destination.json";
//    private String moduleName;
//
//    @Override
//    public synchronized void init(ProcessingEnvironment processingEnv) {
//        super.init(processingEnv);
//        messager = processingEnv.getMessager();
//        filer = processingEnv.getFiler();
//        moduleName=processingEnv.getOptions().get();
//        messager.printMessage(Diagnostic.Kind.NOTE, moduleName+"11111111111111" );
//        System.out.println("222222222222222" );
//    }
//
//    @Override
//    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
//
//        messager.printMessage(Diagnostic.Kind.NOTE, "3333333333333" );
//        Set<? extends Element> fragmentElements = roundEnv.getElementsAnnotatedWith(FragmentDestination.class);
//        Set<? extends Element> activityElements = roundEnv.getElementsAnnotatedWith(ActivityDestination.class);
//        if (!activityElements.isEmpty() || !fragmentElements.isEmpty()) {
//            HashMap<String, JSONObject> destMap = new HashMap<>();
//            handleDestination(fragmentElements, FragmentDestination.class, destMap);
//            handleDestination(activityElements, ActivityDestination.class, destMap);
//
//
//            FileOutputStream fos = null;
//            OutputStreamWriter writer = null;
//            //navigation2_self/src/main/assets
//            try {
//                FileObject resource = filer.createResource(StandardLocation.CLASS_OUTPUT, "", OUT_PUT_FILE_NAME);
//                String resourcePath = resource.toUri().getPath();
//                messager.printMessage(Diagnostic.Kind.NOTE, "resourcePath：" + resourcePath);
//                System.out.println("resourcePath：" + resourcePath);
//                String appPath = resourcePath.substring(0, resourcePath.indexOf("navigation2_self") + "navigation2_self".length()+1);
//                String assetsPath = appPath + "src/main/assets/";
//                File file = new File(assetsPath);
//                if (!file.exists()) {
//                    file.mkdirs();
//                }
//
//                File outPutFile = new File(file, OUT_PUT_FILE_NAME);
//                if (outPutFile.exists()) {
//                    outPutFile.delete();
//                }
//                outPutFile.createNewFile();
//
//                String content = JSON.toJSONString(destMap);
//
//                fos = new FileOutputStream(outPutFile);
//                writer = new OutputStreamWriter(fos, "UTF-8");
//                writer.write(content);
//                writer.flush();
//            } catch (IOException e) {
//                e.printStackTrace();
//            } finally {
//                if (writer != null) {
//                    try {
//                        writer.close();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//                if (fos != null) {
//                    try {
//                        fos.close();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        }
//
//        return true;
//    }
//
//    private void handleDestination(Set<? extends Element> elements, Class<? extends Annotation> annotationClass, HashMap<String, JSONObject> destMap) {
//        for (Element element : elements) {
//            TypeElement typeElement = (TypeElement) element;
//            String pageUrl = null;
//            String clazName = typeElement.getQualifiedName().toString();
//            int id = Math.abs(clazName.hashCode());
//            boolean needLogin = false;
//            boolean asStarter = false;
//            boolean isFrgment = false;
//
//            Annotation annotation = typeElement.getAnnotation(annotationClass);
//            if (annotation instanceof FragmentDestination) {
//                FragmentDestination dest = (FragmentDestination) annotation;
//                pageUrl = dest.pageUrl();
//                asStarter = dest.asStarter();
//                needLogin = dest.needLogin();
//                isFrgment = true;
//            } else if (annotation instanceof ActivityDestination) {
//                ActivityDestination dest = (ActivityDestination) annotation;
//                pageUrl = dest.pageUrl();
//                asStarter = dest.asStarter();
//                needLogin = dest.needLogin();
//            }
//
//            if (destMap.containsKey(pageUrl)) {
//                messager.printMessage(Diagnostic.Kind.ERROR, "不同的页面不允许使用相同的pageUrl");
//            } else {
//                JSONObject object = new JSONObject();
//                object.put("pageUrl", pageUrl);
//                object.put("asStarter", asStarter);
//                object.put("needLogin", needLogin);
//                object.put("clazName", clazName);
//                object.put("isFrgment", isFrgment);
//                destMap.put(pageUrl, object);
//            }
//        }
//    }
//
//    @Override
//    public Set<String> getSupportedOptions() {
//        return super.getSupportedOptions();
//    }
//}
