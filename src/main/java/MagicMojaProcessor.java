import com.google.auto.service.AutoService;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.util.Set;

@AutoService(Processor.class)
public class MagicMojaProcessor  extends AbstractProcessor {    // Processor인터페이스 implement도 가능

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return Set.of(Magic.class.getName());
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        /*
        1. @Magic annotation이 기입된 모든 element들을 확인한다
            element : 소스 코드를 이루는 각 요소들 - 패키지, 메소드, 클래스, 인터페이스, 필드 등
         */
        Set<? extends Element> withMagicElements = roundEnv.getElementsAnnotatedWith(Magic.class);
        for (Element element : withMagicElements) {
            Name elementName = element.getSimpleName();

            // interface에 적용된 @Magic만 허용
            if (element.getKind() != ElementKind.INTERFACE) {
                processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR
                        , "@Magic annotation can not be used on " + elementName);
            } else {
                processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE
                        , "Processing " + elementName);
            }
        }

        return true;    // 여기서 @Magic을 처리하여, 다른 round에서는 이에 대한 처리를 요청하지 않도록 한다
    }
}