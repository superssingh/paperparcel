/*
 * Copyright (C) 2016 Bradley Campbell.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package paperparcel;

import com.google.auto.common.BasicAnnotationProcessor;
import com.google.auto.service.AutoService;
import com.google.common.collect.ImmutableList;
import com.google.googlejavaformat.java.filer.FormattingFiler;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.Processor;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.TypeParameterElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;

/**
 * The annotation processor responsible for generating the classes that drive the PaperParcel
 * implementation.
 */
@AutoService(Processor.class)
public class PaperParcelProcessor extends BasicAnnotationProcessor {
  private static final String KAPT1_GENERATED_OPTION = "kapt.kotlin.generated";
  private static final String KAPT2_PROCESSING_ENVIRONMENT =
      "org.jetbrains.kotlin.annotation.processing.impl.KotlinProcessingEnvironment";

  @Override public SourceVersion getSupportedSourceVersion() {
    return SourceVersion.latestSupported();
  }

  @Override protected Iterable<? extends ProcessingStep> initSteps() {
    Messager messager = processingEnv.getMessager();
    Types types = processingEnv.getTypeUtils();
    Elements elements = processingEnv.getElementUtils();
    Filer filer = new FormattingFiler(processingEnv.getFiler());

    // Older kapt versions had a lot of bugs that prevent PaperParcel from working. Try to
    // detect these here and show a helpful message to the user informing them they need to
    // upgrade their kotlin version.
    boolean kapt1 = processingEnv.getOptions().get(KAPT1_GENERATED_OPTION) != null;
    boolean kapt2 = processingEnv.getClass().getName().equals(KAPT2_PROCESSING_ENVIRONMENT);
    if (kapt1) {
      messager.printMessage(Diagnostic.Kind.ERROR, "PaperParcel is not compatible with legacy "
          + "kapt. Please upgrade to kotlin 1.0.5 (or greater) and apply the 'kotlin-kapt' gradle "
          + "plugin.");
      return ImmutableList.of();
    }
    if (kapt2) {
      if (hasKt13804()) {
        messager.printMessage(Diagnostic.Kind.ERROR, "PaperParcel is not compatible kotlin 1.0.4. "
            + "Please upgrade to kotlin 1.0.5 (or greater).");
        return ImmutableList.of();
      } else {
        messager.printMessage(Diagnostic.Kind.WARNING, "kapt2 has been replaced with a newer "
            + "version in kotlin 1.0.6 that is a lot more stable. It is highly recommended that you "
            + "upgrade.");
      }
    }

    AdapterRegistry adapterRegistry = new AdapterRegistry(elements, types);

    RegisterAdapterValidator registerAdapterValidator =
        new RegisterAdapterValidator(elements, types);
    PaperParcelValidator paperParcelValidator =
        new PaperParcelValidator(elements, types);

    FieldDescriptor.Factory fieldDescriptorFactory = new FieldDescriptor.Factory(types);
    WriteInfo.Factory writeInfoFactory = new WriteInfo.Factory(types, fieldDescriptorFactory);
    ReadInfo.Factory readInfoFactory = new ReadInfo.Factory(types, fieldDescriptorFactory);
    Adapter.Factory adapterFactory = new Adapter.Factory(elements, types, adapterRegistry);
    PaperParcelDescriptor.Factory paperParcelDescriptorFactory =
        new PaperParcelDescriptor.Factory(
            elements,
            types,
            adapterFactory,
            writeInfoFactory,
            readInfoFactory);

    PaperParcelGenerator paperParcelGenerator = new PaperParcelGenerator(filer);

    return ImmutableList.of(
        new RegisterAdapterProcessingStep(
            messager,
            registerAdapterValidator,
            adapterRegistry),
        new PaperParcelProcessingStep(
            messager,
            paperParcelValidator,
            paperParcelDescriptorFactory,
            paperParcelGenerator));
  }

  private boolean hasKt13804() {
    // Checks for KT-13804. This affects only kotlin 1.0.4.
    Elements elements = processingEnv.getElementUtils();
    Types types = processingEnv.getTypeUtils();
    TypeElement comparable = elements.getTypeElement("java.lang.Comparable");
    TypeParameterElement comparableParam = comparable.getTypeParameters().get(0);
    DeclaredType integerType = (DeclaredType) elements.getTypeElement("java.lang.Integer").asType();
    return types.asMemberOf(integerType, comparableParam).getKind() == TypeKind.TYPEVAR;
  }
}
