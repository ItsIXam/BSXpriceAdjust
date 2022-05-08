package Price;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties({"name", "category_id"})
public class Item {
    private String no;
    private String type;

    public Item() {
    }

    @JsonProperty("no")
    public String getNo() {
        return no;
    }

    @JsonProperty("no")
    public void setNo(String no) {
        this.no = no;
    }

    @JsonProperty("type")
    public String getType() {
        return type;
    }

    @JsonProperty("type")
    public void setType(String type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Item item)) return false;

        if (no != null ? !no.equals(item.no) : item.no != null) return false;
        return type != null ? type.equals(item.type) : item.type == null;
    }
}
