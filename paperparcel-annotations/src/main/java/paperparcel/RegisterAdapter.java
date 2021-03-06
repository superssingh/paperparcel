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

import java.io.Serializable;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.SOURCE;

/**
 * <p>Annotate {@code TypeAdapter} classes with this to register them in PaperParcel's type
 * system.</p>
 */
@Documented
@Retention(SOURCE)
@Target(TYPE)
public @interface RegisterAdapter {
  /**
   * <p>Sets the priority of the annotated {@code TypeAdapter}. If multiple type adapters can
   * handle the same type, priority is what decides which will be used (otherwise PaperParcel will
   * use whichever it finds first). {@code TypeAdapter}s with higher priority will be favoured.</p>
   *
   * <p>The default priority is {@code Priority.LOW}.</p>
   */
  Priority priority() default Priority.LOW;

  /**
   * Return {@code true} if this type {@code TypeAdapter} can handle {@code null}s or doesn't
   * need to (i.e. the {@code TypeAdapter} is parcelling a non-nullable type). If this returns
   * {@code false} (the default), then PaperParcel will do null-checking for you. Returning
   * {@code true} from this method can help by preventing double (or completely unnecessary)
   * null-checking.
   */
  boolean nullSafe() default false;

  enum Priority {
    /**
     * <p>The lowest priority.</p>
     *
     * <p>The PaperParcel processor will consider these {@code TypeAdapter}s last when determining
     * how to handle a particular type.</p>
     *
     * <p>Useful when defining an expensive {@code TypeAdapter} which should be used only when
     * nothing else could handle a particular type (e.g. a {@code TypeAdapter} that handles
     * {@link Serializable} objects).</p>
     */
    MIN(0),

    /**
     * <p>The PaperParcel processor will consider these {@code TypeAdapter}s after {@code MAX}
     * priority, {@code HIGH} priority, and built-in {@code TypeAdapter}s, when determining how
     * to handle a particular type.</p>
     */
    LOW(100),

    /**
     * <p>The PaperParcel processor will consider these {@code TypeAdapter}s after {@code MAX}
     * priority {@code TypeAdapter}s, but before built-in {@code TypeAdapter}s, when determining
     * how to handle a particular type.</p>
     *
     * <p>This is useful for overriding built-in behaviour.</p>
     */
    HIGH(200),

    /**
     * <p>The highest priority.</p>
     *
     * <p>The PaperParcel processor will consider these {@code TypeAdapter}s first when determining
     * how to handle a particular type.</p>
     *
     * <p>Useful when you want to ensure the annotated {@code TypeAdapter} will be used over every
     * other {@code TypeAdapter} that can handle a particular type.</p>
     */
    MAX(300);

    int value;

    Priority(int value) {
      this.value = value;
    }
  }
}
