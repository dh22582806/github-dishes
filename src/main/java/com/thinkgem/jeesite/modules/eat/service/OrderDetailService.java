/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.thinkgem.jeesite.modules.eat.service;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.service.BaseService;

/**
 * 订单Service
 * 
 * @author ThinkGem
 * @version 2014-05-13
 */
@Component
@Transactional(readOnly = true)
public class OrderDetailService extends BaseService {

}
