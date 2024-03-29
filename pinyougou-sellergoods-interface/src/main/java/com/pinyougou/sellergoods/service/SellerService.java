package com.pinyougou.sellergoods.service;
import java.util.List;
import com.pinyougou.pojo.TbSeller;

import com.github.pagehelper.PageInfo;
import com.pinyougou.core.service.CoreService;
/**
 * 服务层接口
 * @author Administrator
 *
 */
public interface SellerService extends CoreService<TbSeller> {

	/**
	 * 审核的功能 因为只修改 审核状态 其他不能修改所以要重新写一个修改方法
	 */

	void updateStatus(String sellerId, String status);


	
	/**
	 * 返回分页列表
	 * @return
	 */
	 PageInfo<TbSeller> findPage(Integer pageNo, Integer pageSize);
	
	

	/**
	 * 分页
	 * @param pageNo 当前页 码
	 * @param pageSize 每页记录数
	 * @return
	 */
	PageInfo<TbSeller> findPage(Integer pageNo, Integer pageSize, TbSeller Seller);
	
}

