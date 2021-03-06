package nz.bradcampbell.benchmarkdemo.model.parceler;

import com.google.gson.annotations.SerializedName;
import java.util.List;
import org.parceler.Parcel;

@Parcel
public class User {
  public String id;
  public int index;
  public String guid;
  @SerializedName("is_active") public boolean isActive;
  public String balance;
  @SerializedName("picture") public String pictureUrl;
  public int age;
  public Name name;
  public String company;
  public String email;
  public String address;
  public String about;
  public String registered;
  public double latitude;
  public double longitude;
  public List<String> tags;
  public List<Integer> range;
  public List<Friend> friends;
  public List<Image> images;
  public String greeting;
  @SerializedName("favorite_fruit") public String favoriteFruit;
  @SerializedName("eye_color") public String eyeColor;
  public String phone;
}
