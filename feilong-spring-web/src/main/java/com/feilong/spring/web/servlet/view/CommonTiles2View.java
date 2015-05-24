package com.feilong.spring.web.servlet.view;
///**
// * Copyright (c) 2008-2014 FeiLong, Inc. All Rights Reserved.
// * <p>
// * 	This software is the confidential and proprietary information of FeiLong Network Technology, Inc. ("Confidential Information").  <br>
// * 	You shall not disclose such Confidential Information and shall use it 
// *  only in accordance with the terms of the license agreement you entered into with FeiLong.
// * </p>
// * <p>
// * 	FeiLong MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE SOFTWARE, EITHER EXPRESS OR IMPLIED, 
// * 	INCLUDING BUT NOT LIMITED TO THE IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
// * 	PURPOSE, OR NON-INFRINGEMENT. <br> 
// * 	FeiLong SHALL NOT BE LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING
// * 	THIS SOFTWARE OR ITS DERIVATIVES.
// * </p>
// */
//package com.feilong.spring.web.view;
//
//import java.util.Map;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import javax.servlet.jsp.jstl.core.Config;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.web.servlet.view.tiles2.TilesView;
//
//import com.feilong.commons.core.enumeration.CharsetType;
//import com.feilong.servlet.ServletContextUtil;
//import com.feilong.servlet.http.RequestUtil;
//import com.feilong.servlet.http.SessionUtil;
//import com.feilong.tools.json.JsonUtil;
//
///**
// * The Class CommonTiles2View.
// */
//public class CommonTiles2View extends TilesView{
//
//	/** The Constant log. */
//	private static final Logger	log	= LoggerFactory.getLogger(CommonTiles2View.class);
//
//	/*
//	 * (non-Javadoc)
//	 * @see org.springframework.web.servlet.view.tiles2.TilesView#renderMergedOutputModel(java.util.Map,
//	 * javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
//	 */
//	protected void renderMergedOutputModel(Map<String, Object> model,HttpServletRequest request,HttpServletResponse response)
//			throws Exception{
//		super.renderMergedOutputModel(model, request, response);
//
//		if (log.isDebugEnabled()){
//			Map<String, Object> attributeMap = RequestUtil.getAttributeMap(request);
//			// 这个key format 有问题
//			attributeMap.remove(Config.FMT_LOCALIZATION_CONTEXT + ".request");
//			// javax.servlet.jsp.jstl.fmt.localizationContext.request
//			// java.lang.IllegalAccessException:
//			// Class loxia.support.json.JSONObject can not access a member of class
//			// org.springframework.web.servlet.support.JstlUtils$SpringLocalizationContext
//			// with modifiers "public"
//			// model 已经 exposeModelAsRequestAttributes
//			Object[] argsObjects = { RequestUtil.getRequestFullURL(request, CharsetType.UTF8),
//					// , JsonFormatUtil.defaultPropFilterStr + ",-" + Config.FMT_LOCALIZATION_CONTEXT + ".request"
//					JsonUtil.format(attributeMap),
//					JsonUtil.format(SessionUtil.getAttributeMap(request.getSession())),
//					JsonUtil.format(ServletContextUtil.getAttributeMap(request.getSession().getServletContext())) };
//
//			log.debug(
//					"requestAllURL:{},\n request attributeMap:{},\n session attributeMap:{} , \n servletContext attributeMap:{} ",
//					argsObjects);
//		}
//	}
//}
