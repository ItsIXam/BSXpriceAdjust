package entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * The type Item.
 */
@JsonIgnoreProperties({"name", "category_id"})
public class Item {

    private String no;
    private String type;

    /**
     * Instantiates a new Item.
     */
    public Item() {
    }

    /**
     * Gets no.
     *
     * @return the no
     */
    @JsonProperty("no")
    public String getNo() {
        return no;
    }

    /**
     * Sets no.
     *
     * @param no the no
     */
    @JsonProperty("no")
    public void setNo(String no) {
        this.no = no;
    }

    /**
     * Gets type.
     *
     * @return the type
     */
    @JsonProperty("type")
    public String getType() {
        return type;
    }

    /**
     * Sets type.
     *
     * @param type the type
     */
    @JsonProperty("type")
    public void setType(String type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Item item)) {
            return false;
        }

        if (no != null ? !no.equals(item.no) : item.no != null) {
            return false;
        }
        return type != null ? type.equals(item.type) : item.type == null;
    }
}
