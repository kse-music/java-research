package cn.hiboot.java.research.db.mongo;

import java.util.List;
import java.util.Map;

/**
 * describe about this class
 *
 * @author DingHao
 * @since 2019/7/15 13:45
 */
public class BasicParam {

    private Long id;
    private Long conceptId;

    private Integer type;

    private String name;

    private String meaningTag;

    private String abs;
    private String imageUrl;

    private List<String> synonyms;
    private Map<Integer,String> attributes;
    private Map<String,String> privateAttributes;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getConceptId() {
        return conceptId;
    }

    public void setConceptId(Long conceptId) {
        this.conceptId = conceptId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMeaningTag() {
        return meaningTag;
    }

    public void setMeaningTag(String meaningTag) {
        this.meaningTag = meaningTag;
    }

    public String getAbs() {
        return abs;
    }

    public void setAbs(String abs) {
        this.abs = abs;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public List<String> getSynonyms() {
        return synonyms;
    }

    public void setSynonyms(List<String> synonyms) {
        this.synonyms = synonyms;
    }

    public Map<Integer, String> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<Integer, String> attributes) {
        this.attributes = attributes;
    }

    public Map<String, String> getPrivateAttributes() {
        return privateAttributes;
    }

    public void setPrivateAttributes(Map<String, String> privateAttributes) {
        this.privateAttributes = privateAttributes;
    }
}
