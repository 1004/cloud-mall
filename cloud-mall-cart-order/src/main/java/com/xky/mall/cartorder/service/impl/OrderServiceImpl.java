package com.xky.mall.cartorder.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.zxing.WriterException;
import com.xky.mall.cartorder.config.OrderUploadFileConstants;
import com.xky.mall.cartorder.feign.ProductFeignClient;
import com.xky.mall.cartorder.feign.UserFeignClient;
import com.xky.mall.cartorder.model.dao.CartMapper;
import com.xky.mall.cartorder.model.dao.OrderItemMapper;
import com.xky.mall.cartorder.model.dao.OrderMapper;
import com.xky.mall.cartorder.model.pojo.Order;
import com.xky.mall.cartorder.model.pojo.OrderItem;
import com.xky.mall.cartorder.model.request.CreateOrderReq;
import com.xky.mall.cartorder.model.vo.CartVO;
import com.xky.mall.cartorder.model.vo.OrderItemVO;
import com.xky.mall.cartorder.model.vo.OrderVO;
import com.xky.mall.cartorder.service.OrderService;
import com.xky.mall.catp.config.UploadFileConstants;
import com.xky.mall.catp.model.pojo.Product;
import com.xky.mall.common.common.Constants;
import com.xky.mall.common.exception.MallException;
import com.xky.mall.common.exception.MallExceptionEnum;
import com.xky.mall.utils.OrderCodeFactory;
import com.xky.mall.utils.QRCodeGenerator;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author xiekongying
 * @version 1.0
 * @date 2021/7/26 3:48 下午
 */
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private CartMapper cartMapper;

    @Autowired
    private ProductFeignClient productFeignClient;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderItemMapper orderItemMapper;

    @Autowired
    private UserFeignClient userFeignClient;

    @Value("${file.upload.ip}")
    private String ip;// 支付ip地址
    @Value("${file.upload.port}")
    private Integer port;//
    /**
     * 创建订单
     *
     * @param orderReq 订单号
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public String create(CreateOrderReq orderReq) {
        //①拿到用户id
        Integer userId = userFeignClient.getUserByFeign().getId();
        //② 获取购物车中的选中的商品 并对商品做校验
        List<CartVO> cartVOS = selectCartVos(userId);
        //③ 判断商品是否存在，上下架状态， 库存
        validProduct(cartVOS);
        //④ 将商品转为中间表的item对象
        List<OrderItem> orderItems = cartVOListToOrderItems(cartVOS);
        //⑤ 扣库存
        deleteStock(orderItems);
        //⑥ 清除购物车中已选择的商品
        clearCarSelectP(cartVOS);
        //⑦生成订单对象,并保存
        Order order = generateOrder(orderReq, orderItems, userId);
        //⑨ 插入商品和订单关系表 order_item
        String orderNo = orderItemInsert(orderItems, order);
        //10 返回订单编码
        return orderNo;
    }

    private List<CartVO> selectCartVos(Integer userId) {
        List<CartVO> localVOs = cartMapper.selectList(userId);
        List<CartVO> selectVOs = new ArrayList<>();
        for (int i = 0; i < localVOs.size(); i++) {
            CartVO cartVO = localVOs.get(i);
            if (cartVO.getSelected() == Constants.Cart.CHECKED) {
                selectVOs.add(cartVO);
            }
        }
        if (CollectionUtils.isEmpty(selectVOs)) {
            throw new MallException(MallExceptionEnum.ORDER_NO_SELECT);
        }
        return selectVOs;
    }

    /**
     * 校验商品
     */
    private void validProduct(List<CartVO> cartVOS) {
        for (int i = 0; i < cartVOS.size(); i++) {
            CartVO cartVO = cartVOS.get(i);
            Product product = productFeignClient.proDetailByFeign(cartVO.getProductId());
            if (product == null) {
                throw new MallException(MallExceptionEnum.CART_PRO_NOT_EXIST);
            }
            if (product.getStatus() == Constants.SaleStatus.NOT_SALE) {
                throw new MallException(MallExceptionEnum.CART_PRO_HAS_SALE);
            }
            //库存不足
            if (product.getStock() < cartVO.getQuantity()) {
                throw new MallException(MallExceptionEnum.CART_PRO_NO_ENOUGH);
            }
        }
    }

    private List<OrderItem> cartVOListToOrderItems(List<CartVO> cartVOS) {
        List<OrderItem> orderItems = new ArrayList<>();
        for (int i = 0; i < cartVOS.size(); i++) {
            CartVO cartVO = cartVOS.get(i);
            OrderItem orderItem = new OrderItem();
            orderItem.setProductId(cartVO.getProductId());
            orderItem.setProductImg(cartVO.getProductImage());
            orderItem.setProductName(cartVO.getProductName());
            orderItem.setUnitPrice(cartVO.getProductPrice());
            orderItem.setTotalPrice(cartVO.getProductPrice() * cartVO.getQuantity());
            orderItem.setQuantity(cartVO.getQuantity());
            orderItems.add(orderItem);
        }
        return orderItems;
    }

    //扣库存
    private void deleteStock(List<OrderItem> orderItems) {
        for (int i = 0; i < orderItems.size(); i++) {
            OrderItem orderItem = orderItems.get(i);
            Product product = productFeignClient.proDetailByFeign(orderItem.getProductId());
            int quantity = product.getStock() - orderItem.getQuantity();
            if (quantity < 0) {
                throw new MallException(MallExceptionEnum.ORDER_NO_ENOUGH);
            }
            product.setStock(quantity);
            productFeignClient.updateByPrimaryKeySelectiveByFeign(product.getId(),product.getStock());
        }
    }

    //清空选择的购物车
    private void clearCarSelectP(List<CartVO> cartVOS) {
        for (int i = 0; i < cartVOS.size(); i++) {
            CartVO cartVO = cartVOS.get(i);
            cartMapper.deleteByPrimaryKey(cartVO.getId());
        }
    }

    private Order generateOrder(CreateOrderReq orderReq, List<OrderItem> orderItems, Integer userId) {
        Order order = new Order();
        order.setOrderNo(OrderCodeFactory.getOrderCode(Long.valueOf(userId + "")));
        order.setUserId(userId);
        order.setTotalPrice(getTotalPrice(orderItems));
        order.setOrderStatus(Constants.OrderStatusEnum.NOT_PAY.getCode());
        order.setReceiverName(orderReq.getReceiverName());
        order.setReceiverAddress(orderReq.getReceiverAddress());
        order.setReceiverMobile(orderReq.getReceiverMobile());
        order.setPostage(0);
        order.setPaymentType(1); //支付方式
        int count = orderMapper.insertSelective(order);
        return order;
    }

    private Integer getTotalPrice(List<OrderItem> orderItems) {
        Integer total = 0;
        for (int i = 0; i < orderItems.size(); i++) {
            total += orderItems.get(i).getTotalPrice();
        }
        return total;
    }

    private String orderItemInsert(List<OrderItem> orderItems, Order order) {
        for (int i = 0; i < orderItems.size(); i++) {
            OrderItem orderItem = orderItems.get(i);
            orderItem.setOrderNo(order.getOrderNo());
            orderItemMapper.insertSelective(orderItem);
        }
        return order.getOrderNo();
    }

    /**
     * 订单详情
     *
     * @param orderNo
     * @return
     */
    @Override
    public OrderVO detail(String orderNo) {
        Order order = orderMapper.selectByOrderNo(orderNo);
        if (order == null) {
            throw new MallException(MallExceptionEnum.ORDER_NO_EXIST);
        }
        //判断是否为当前用户的
        if (!order.getUserId().equals(userFeignClient.getUserByFeign().getId())) {
            throw new MallException(MallExceptionEnum.ORDER_NO_PER);
        }
        OrderVO orderVO = generateOrderVO(order);
        return orderVO;
    }

    private OrderVO generateOrderVO(Order order) {
        OrderVO orderVO = new OrderVO();
        BeanUtils.copyProperties(order, orderVO);
        orderVO.setOrderStatusName(Constants.OrderStatusEnum.codeOfValue(order.getOrderStatus()).getValue());
        List<OrderItem> orderItems = orderItemMapper.selectByOrderNo(order.getOrderNo());
        List<OrderItemVO> orderItemVOS = new ArrayList<>();
        for (int i = 0; i < orderItems.size(); i++) {
            OrderItem orderItem = orderItems.get(i);
            OrderItemVO orderItemVO = new OrderItemVO();
            BeanUtils.copyProperties(orderItem, orderItemVO);
            orderItemVOS.add(orderItemVO);
        }
        orderVO.setOrderItemVOList(orderItemVOS);
        return orderVO;
    }


    /**
     * 普通用户的订单列表
     * @param page
     * @param pageSize
     * @return
     */
    @Override
    public PageInfo listForCustomer(Integer page, Integer pageSize){
        PageHelper.startPage(page,pageSize);
        List<Order> orders = orderMapper.selectByCustomer(userFeignClient.getUserByFeign().getId());
        List<OrderVO> orderVOS = ordersToOrderVOs(orders);
        return PageInfo.of(orderVOS);
    }

    private List<OrderVO> ordersToOrderVOs(List<Order> orders) {
        List<OrderVO> orderVOS = new ArrayList<>();
        for (int i= 0 ;i<orders.size() ;i++){
            Order order = orders.get(i);
            OrderVO orderVO = generateOrderVO(order);
            orderVOS.add(orderVO);
        }
        return orderVOS;
    }

    @Override
    public void cancelOrder(String orderNo){
        Order order = orderMapper.selectByOrderNo(orderNo);
        if (order == null){
            throw new MallException(MallExceptionEnum.ORDER_NO_EXIST);
        }
        //判断是否为当前用户的
        if (!order.getUserId().equals(userFeignClient.getUserByFeign().getId())) {
            throw new MallException(MallExceptionEnum.ORDER_NO_PER);
        }
        //状态要是未付款
        if (!order.getOrderStatus().equals(Constants.OrderStatusEnum.NOT_PAY.getCode())){
            throw new MallException(MallExceptionEnum.ORDER_NO_CANCEL);
        }

        order.setOrderStatus(Constants.OrderStatusEnum.CANCELED.getCode());
        order.setEndTime(new Date());
        orderMapper.updateByPrimaryKeySelective(order);
    }


    @Override
    public String qrcode(String orderNo){
        //① 获取请求参数
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        String address = ip+":"+port;
        String payURl = "http://"+address+"/co/order/pay?orderNo="+orderNo;
        try {
            QRCodeGenerator.generateQrCodeImage(payURl,350,350, OrderUploadFileConstants.FILE_UPLOAD_DIR+orderNo+".png");
        } catch (WriterException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //图片url和生成的图片本地已经配置好映射关系
        String pngAddress = "http://"+address+"/co/image/"+orderNo+".png";
        return pngAddress;
    }

    /**
     * 管理者查询订单
     * @param page
     * @param pageSize
     * @return
     */
    @Override
    public PageInfo listForAdmin(Integer page, Integer pageSize){
        PageHelper.startPage(page,pageSize);
        List<Order> orders = orderMapper.selectByAdmin();
        List<OrderVO> orderVOS = ordersToOrderVOs(orders);
        return PageInfo.of(orderVOS);
    }


    @Override
    public void pay(String orderNo){
        Order order = orderMapper.selectByOrderNo(orderNo);
        if (order == null){
            throw new MallException(MallExceptionEnum.ORDER_NO_EXIST);
        }
        //判断是否为当前用户的
        if (!order.getUserId().equals(userFeignClient.getUserByFeign().getId())) {
            throw new MallException(MallExceptionEnum.ORDER_NO_PER);
        }

        //状态要是未付款
        if (!order.getOrderStatus().equals(Constants.OrderStatusEnum.NOT_PAY.getCode())){
            throw new MallException(MallExceptionEnum.ORDER_NO_PER);
        }

        order.setOrderStatus(Constants.OrderStatusEnum.PAID.getCode());
        order.setPayTime(new Date());
        orderMapper.updateByPrimaryKeySelective(order);
    }

    /**
     * 管理员进行发货
     * @param orderNo
     */
    @Override
    public void deliver(String orderNo){
        Order order = orderMapper.selectByOrderNo(orderNo);
        if (order == null){
            throw new MallException(MallExceptionEnum.ORDER_NO_EXIST);
        }
        //状态要是已付款
        if (!order.getOrderStatus().equals(Constants.OrderStatusEnum.PAID.getCode())){
            throw new MallException(MallExceptionEnum.ORDER_NO_PER);
        }

        order.setDeliveryTime(new Date());
        order.setOrderStatus(Constants.OrderStatusEnum.SEND.getCode());
        orderMapper.updateByPrimaryKeySelective(order);
    }

    /**
     * 对订单做完结
     * 管理员可以对所有订单完结
     * 用户也可以对自己的订单完结
     * @param orderNo
     */
    @Override
    public void finish(String orderNo){
        Order order = orderMapper.selectByOrderNo(orderNo);
        if (order == null){
            throw new MallException(MallExceptionEnum.ORDER_NO_EXIST);
        }
        //状态要是已发货
        if (!order.getOrderStatus().equals(Constants.OrderStatusEnum.SEND.getCode())){
            throw new MallException(MallExceptionEnum.ORDER_NO_PER);
        }

        if (!userFeignClient.getUserByFeign().getRole().equals(2) && !order.getUserId().equals(userFeignClient.getUserByFeign().getId())){
            //不是管理员，当前订单的用户也不是登录用户
            throw new MallException(MallExceptionEnum.ORDER_NO_PER);
        }

        order.setOrderStatus(Constants.OrderStatusEnum.FINISHED.getCode());
        order.setEndTime(new Date());
        orderMapper.updateByPrimaryKeySelective(order);
    }


}
