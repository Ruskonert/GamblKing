package com.ruskonert.GamblKing.engine.entity.card.framework;

import com.ruskonert.GamblKing.engine.entity.card.component.AttackType;
import com.ruskonert.GamblKing.engine.entity.card.component.CardType;

public abstract class MonsterCard extends CardFramework
{
    public MonsterCard()
    {
        super(CardType.MONSTER);
    }

    private int attack = 0;
    public int getAttack() { return this.attack; }
    public void setAttack(int attack) { this.attack = attack; }

    private int defense = 0;
    public void setDefense(int defense) { this.defense = defense; }
    public int getDefense() { return defense; }

    // 시스템이 조절합니다. 건드리지 않는 것을 추천합니다.
    public AttackType getType() { return type; }

    // 공격의 유형입니다. 공격의 유형은 수비형과 공격형이 있습니다.
    // 시스템이 조절합니다. 건드리지 않는 것을 추천합니다.
    private AttackType type = AttackType.ATTACKER;

    // 시스템이 조절합니다. 건드리지 않는 것을 추천합니다.
    public AttackType getAttackType() { return this.type; }
}
