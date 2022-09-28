package com.hoiae.mygoods.member.dto;

public class MyCharacterDTO {
    private  String characterImageURL;
    public MyCharacterDTO() {
    }
    public MyCharacterDTO(String characterImageURL) {
        this.characterImageURL = characterImageURL;
    }

    public String getCharacterImageURL() {
        return characterImageURL;
    }

    public void setCharacterImageURL(String characterImageURL) {
        this.characterImageURL = characterImageURL;
    }

    @Override
    public String toString() {
        return "CharacterDTO{" +
                "characterImageURL='" + characterImageURL + '\'' +
                '}';
    }
}
