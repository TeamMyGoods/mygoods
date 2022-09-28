package com.hoiae.mygoods.product.dto;

public class CharacterDTO {
    private int characterCode;
    private int MemberNo;
    private String ModelName;
    private String characterImageUrl;

    public CharacterDTO() {
    }

    public CharacterDTO(int characterCode, int memberNo, String modelName, String characterImageUrl) {
        this.characterCode = characterCode;
        MemberNo = memberNo;
        ModelName = modelName;
        this.characterImageUrl = characterImageUrl;
    }

    public int getCharacterCode() {
        return characterCode;
    }

    public void setCharacterCode(int characterCode) {
        this.characterCode = characterCode;
    }

    public int getMemberNo() {
        return MemberNo;
    }

    public void setMemberNo(int memberNo) {
        MemberNo = memberNo;
    }

    public String getModelName() {
        return ModelName;
    }

    public void setModelName(String modelName) {
        ModelName = modelName;
    }

    public String getCharacterImageUrl() {
        return characterImageUrl;
    }

    public void setCharacterImageUrl(String characterImageUrl) {
        this.characterImageUrl = characterImageUrl;
    }

    @Override
    public String toString() {
        return "CharacterDTO{" +
                "characterCode=" + characterCode +
                ", MemberNo=" + MemberNo +
                ", ModelName='" + ModelName + '\'' +
                ", characterImageUrl='" + characterImageUrl + '\'' +
                '}';
    }
}
