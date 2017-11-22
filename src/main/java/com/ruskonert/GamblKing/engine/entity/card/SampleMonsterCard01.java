package com.ruskonert.GamblKing.engine.entity.card;

import com.ruskonert.GamblKing.engine.entity.card.framework.MonsterCard;

public class SampleMonsterCard01 extends MonsterCard
{
    public SampleMonsterCard01()
    {
        this.setName("샘플 몬스터 카드");
        this.setDescription("몬스터 카드입니다.");
        this.setImage("01.jpg");
        this.setAttack(1500);
        this.setDefense(2000);
    }
}
