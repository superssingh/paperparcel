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

package paperparcel.internal;

import android.os.Parcel;
import android.support.annotation.NonNull;
import paperparcel.TypeAdapter;

@SuppressWarnings({ "WeakerAccess", "unused" }) // Used by generated code
public final class ArrayAdapter<T> implements TypeAdapter<T[]> {
  public ArrayAdapter(Class<T> componentType, TypeAdapter<T> componentAdapter) {
    throw new RuntimeException("Stub!");
  }

  @Override public T[] readFromParcel(@NonNull Parcel source) {
    throw new RuntimeException("Stub!");
  }

  @Override public void writeToParcel(@NonNull T[] value, @NonNull Parcel dest, int flags) {
    throw new RuntimeException("Stub!");
  }
}
