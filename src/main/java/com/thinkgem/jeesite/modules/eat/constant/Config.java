package com.thinkgem.jeesite.modules.eat.constant;

public interface Config {
	/**
	 * 下台
	 */
	public static final int STATE_OPEN_TABLE = 1;
	/**
	 * 下单
	 */
	public static final int STATE_MAKE_ORDER = 2;
	/**
	 * 离台
	 */
	public static final int STATE_LEAVE_TABLE = 3;
	/**
	 * 预结账
	 */
	public static final int STATE_PRE_CHARGE = 4;
	/**
	 * 加单
	 */
	public static final int STATE_ADD_ORDER = 5;
	/**
	 * 已结账
	 */
	public static final int STATE_CHARGED = 6;
	/**
	 * 订单上齐
	 */
	public static final int STATE_ORDER_FINISH = 7;
	/**
	 * 桌台未入座
	 */
	public static final Integer[] STATES_FREE = new Integer[] {
			STATE_LEAVE_TABLE, STATE_CHARGED };
	/**
	 * 桌台未入座
	 */
	public static final Integer[] STATES_BUSY = new Integer[] {
			STATE_OPEN_TABLE, STATE_MAKE_ORDER, STATE_PRE_CHARGE,
			STATE_ADD_ORDER, STATE_ORDER_FINISH };

	/**
	 * 未登录
	 */
	public static final int CODE_NOTLOGIN = 8;
	
}
