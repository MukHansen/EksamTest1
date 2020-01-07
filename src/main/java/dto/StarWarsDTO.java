package dto;

import java.util.Objects;

/**
 *
 * @author martin
 */
public class StarWarsDTO {
    
    private String name; 
    private String url; 

    public StarWarsDTO(String name, String url) {
        this.name = name;
        this.url = url;
    }

    public StarWarsDTO() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "StarWarsDTO{" + "name=" + name + ", url=" + url + '}';
    }

    @Override
    public int hashCode() {
        int hash = 7;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final StarWarsDTO other = (StarWarsDTO) obj;
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        if (!Objects.equals(this.url, other.url)) {
            return false;
        }
        return true;
    }
    
    
    
}
