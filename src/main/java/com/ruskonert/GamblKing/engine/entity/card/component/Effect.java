package com.ruskonert.GamblKing.engine.entity.card.component;

public enum Effect
{
    /**
     *  그냥 카드를 파괴합니다.
     * {@link com.ruskonert.GamblKing.engine.entity.card.component.Targeting}에서
     * <code>THIS</code>나 <code>OTHER</code>를 통해 자신 필드 또는 상대 필드인지 타켓을 조절할 수 있습니다.
     */
    DESTORY_CARD,

    /**
     * 마법 카드 또는 함정 카드를 파괴합니다.
     * {@link com.ruskonert.GamblKing.engine.entity.card.component.Targeting}에서
     * <code>THIS</code>나 <code>OTHER</code>를 통해 자신 필드 또는 상대 필드인지 타켓을 조절할 수 있습니다.
     */
    DESTORY_SPECIAL_CARD,

    /**
     *  몬스터 카드를 파괴합니다.
     * {@link com.ruskonert.GamblKing.engine.entity.card.component.Targeting}에서
     * <code>THIS</code>나 <code>OTHER</code>를 통해 자신 필드 또는 상대 필드인지 타켓을 조절할 수 있습니다.
     */
    DESTORY_MOSNTER_CARD,

    /**
     *  몬스터의 공격력을 조절합니다.
     * {@link com.ruskonert.GamblKing.engine.entity.card.component.Targeting}에서
     * <code>THIS</code>나 <code>OTHER</code>를 통해 자신 필드 또는 상대 필드인지 타켓 범위를 조절할 수 있습니다.
     */
    CONTROL_ATTACK_VALUE,

    /**
     *  몬스터의 수비력을 조절합니다.
     * {@link com.ruskonert.GamblKing.engine.entity.card.component.Targeting}에서
     * <code>THIS</code>나 <code>OTHER</code>를 통해 자신 필드 또는 상대 필드인지 타켓 범위를 조절할 수 있습니다.
     */
    CONTROL_DEFENSE_VALUE,

    /**
     * 모든 몬스터의 공격력을 조절합니다.
     * {@link com.ruskonert.GamblKing.engine.entity.card.component.Targeting}에서
     * <code>THIS</code>나 <code>OTHER</code>를 통해 타켓 범위를 조절할 수 있습니다.
     */
    TOTAL_CONTROL_ATTACK_VALUE,

    /**
     * 모든 몬스터의 수비력을 조절합니다.
     * {@link com.ruskonert.GamblKing.engine.entity.card.component.Targeting}에서
     * <code>THIS</code>나 <code>OTHER</code>를 통해 타켓 범위를 조절할 수 있습니다.
     */
    TOTAL_CONTROL_DEFENSE_VALUE,
    /**
     *  플레이어에게 데미지를 줍니다.
     * {@link com.ruskonert.GamblKing.engine.entity.card.component.Targeting}에서
     * <code>THIS</code>나 <code>OTHER</code>를 통해 타켓을 지정할 수 있습니다.
     */
    PLAYER_DAMAGE
}
