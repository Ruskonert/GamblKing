package com.ruskonert.GamblKing.engine.entity.card.framework;

import com.ruskonert.GamblKing.engine.entity.card.component.CardType;
import com.ruskonert.GamblKing.engine.entity.card.component.Cost;

public abstract class CardFramework
{
    private String name;

    public CardFramework(CardType cardType)
    {
        this.cardType = cardType;
    }

    public String getName() { return this.name; }
    public void setName(String name) { this.name = name; }

    private String description;
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public void setDescription(String... description)
    {
        StringBuilder builder = new StringBuilder();
        for(String s : description)
        {
            builder.append(s).append("\n");
        }
        this.description = builder.toString();
    }

    // 소환되었을 때, 나오는 이펙트를 설정합니다.
    public void setSummonSound(String path) { /* Complied code */ }

    private boolean hide;
    public boolean isHide() { return this.hide; }

    private Cost cost;
    public void setCost(Cost cost) { this.cost = cost; }

    private CardType cardType;
    public CardType getCardType() { return this.cardType; }

    // image
    // getter image
    public void setImage(String path) { /* Complied code */  getClass().getResource("/style/" + path); }
}
