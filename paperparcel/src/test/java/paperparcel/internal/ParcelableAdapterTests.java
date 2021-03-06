package paperparcel.internal;

import paperparcel.TypeAdapter;
import paperparcel.utils.TestParcelable;
import paperparcel.utils.TestUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static com.google.common.truth.Truth.assertThat;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class ParcelableAdapterTests {
  @Test public void parcelablesAreCorrectlyParcelled() {
    TypeAdapter<TestParcelable> adapter = new ParcelableAdapter<>(TestParcelable.CREATOR);
    TestParcelable expected = new TestParcelable(42);
    TestParcelable result = TestUtils.writeThenRead(adapter, expected);
    assertThat(result).isEqualTo(expected);
  }
}
