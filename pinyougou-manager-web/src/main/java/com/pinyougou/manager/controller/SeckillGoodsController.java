package com.pinyougou.manager.controller;

import java.util.List;
import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageInfo;
import com.pinyougou.common.pojo.MessageInfo;
import com.pinyougou.pojo.TbSeckillGoods;
import com.pinyougou.seckill.service.SeckillGoodsService;
import entity.Result;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * controller
 *
 * @author Administrator
 */
@RestController
@RequestMapping("/seckillGoods")
public class SeckillGoodsController {

    @Autowired
    private DefaultMQProducer producer;

    @Reference
    private SeckillGoodsService seckillGoodsService;

    /**
     * 返回全部列表
     *
     * @return
     */
    @RequestMapping("/findAll")
    public List<TbSeckillGoods> findAll() {
        return this.seckillGoodsService.findAll();
    }



    @RequestMapping("/findPage")
    public PageInfo<TbSeckillGoods> findPage(
        @RequestParam(value = "pageNo", defaultValue = "1", required = true) Integer pageNo,
        @RequestParam(value = "pageSize", defaultValue = "10", required = true) Integer pageSize) {
        return this.seckillGoodsService.findPage(pageNo, pageSize);
    }

    /**
     * 增加
     *
     * @param seckillGoods
     * @return
     */
    @RequestMapping("/add")
    public Result add(@RequestBody TbSeckillGoods seckillGoods) {
        try {
            this.seckillGoodsService.add(seckillGoods);
            return new Result(true, "增加成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "增加失败");
        }
    }

    /**
     * 修改
     *
     * @param seckillGoods
     * @return
     */
    @RequestMapping("/update")
    public Result update(@RequestBody TbSeckillGoods seckillGoods) {
        try {
            this.seckillGoodsService.update(seckillGoods);
            return new Result(true, "修改成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "修改失败");
        }
    }

    /**
     * 获取实体
     *
     * @param id
     * @return
     */
    @RequestMapping("/findOne/{id}")
    public TbSeckillGoods findOne(@PathVariable(value = "id") Long id) {
        return this.seckillGoodsService.findOne(id);
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @RequestMapping("/delete")
    public Result delete(@RequestBody Long[] ids) {
        try {
            this.seckillGoodsService.delete(ids);
            return new Result(true, "删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "删除失败");
        }
    }



    @RequestMapping("/search")
    public PageInfo<TbSeckillGoods> findPage(
        @RequestParam(value = "pageNo", defaultValue = "1", required = true) Integer pageNo,
        @RequestParam(value = "pageSize", defaultValue = "10", required = true) Integer pageSize,
        @RequestBody TbSeckillGoods seckillGoods) {
        return this.seckillGoodsService.findPage(pageNo, pageSize, seckillGoods);
    }

    @RequestMapping("/updateStatus")
    public Result updateStatus(@RequestBody Long[] ids, String status) {
        try {
            for (Long id : ids) {
                TbSeckillGoods goods = new TbSeckillGoods();
                goods.setId(id);
                goods.setStatus(status);
                this.seckillGoodsService.updateByPrimaryKeySelective(goods);
            }
            //发送消息即可  审核商品要进行页面生成
            MessageInfo messageInfo = new MessageInfo("TOPIC_SECKILL", "Tags_genHtml",
                "seckillGoods_updateStatus", ids, MessageInfo.METHOD_ADD);
            this.producer.send(
                new Message(messageInfo.getTopic(), messageInfo.getTags(), messageInfo.getKeys(),
                    JSON.toJSONString(messageInfo).getBytes(RemotingHelper.DEFAULT_CHARSET)));
            return new Result(true, "成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "失败");
        }

    }

}
