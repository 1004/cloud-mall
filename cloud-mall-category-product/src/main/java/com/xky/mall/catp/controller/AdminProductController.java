package com.xky.mall.catp.controller;

import com.xky.mall.catp.config.UploadFileConstants;
import com.xky.mall.catp.model.pojo.Product;
import com.xky.mall.catp.model.request.AddProductReq;
import com.xky.mall.catp.model.request.UpdateProductReq;
import com.xky.mall.catp.service.ProductService;
import com.xky.mall.common.common.CommonResponse;
import com.xky.mall.common.exception.MallExceptionEnum;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.UUID;

/**
 * @author xiekongying
 * @version 1.0
 * @date 2021/7/22 7:20 下午
 * @Desc 后台商品
 */
@RestController
@RequestMapping("/admin/product")
public class AdminProductController {
    @Autowired
    private ProductService productService;

    @Value("${file.upload.ip}")
    String ip;
    @Value("${file.upload.port}")
    Integer port;

    /**
     * 添加商品
     *
     * @param productReq
     * @return
     */
    @PostMapping("/add")
    public CommonResponse addProduct(@Valid @RequestBody AddProductReq productReq) {
        productService.addProduct(productReq);
        return CommonResponse.success();
    }


    /**
     * 图片上传
     *
     * @param request
     * @param file
     * @return
     */
    @PostMapping("/upload")
    public CommonResponse upload(HttpServletRequest request, @RequestParam("file") MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        String suffixName = originalFilename.substring(originalFilename.lastIndexOf("."));
        UUID uuid = UUID.randomUUID();
        String newFileName = uuid.toString() + suffixName;
        //创建文件
        File fileDir = new File(UploadFileConstants.FILE_UPLOAD_DIR);
        if (!fileDir.exists()) {
            if (!fileDir.mkdir()) {
                //创建目录失败
                return CommonResponse.error(MallExceptionEnum.SYSTEM_UPLOAD_F);
            }
        }
        File destFile = new File(UploadFileConstants.FILE_UPLOAD_DIR + newFileName);
        try {
            file.transferTo(destFile);
        } catch (IOException e) {
            e.printStackTrace();
            return CommonResponse.error(MallExceptionEnum.SYSTEM_UPLOAD_F);
        }
        //地址传出去
        try {
            return CommonResponse.success(getHost(new URI(request.getRequestURL() + "")) + "/cp/image/" + newFileName);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return CommonResponse.error(MallExceptionEnum.SYSTEM_UPLOAD_F);
    }

    private URI getHost(URI uri) {
        URI effectiveURI;
        try {
            effectiveURI = new URI(uri.getScheme(), uri.getUserInfo(), ip, port, null, null, null);
        } catch (URISyntaxException e) {
            e.printStackTrace();
            effectiveURI = null;
        }
        return effectiveURI;
    }

    /**
     * 修改商品
     *
     * @param productReq
     * @return
     */
    @ApiOperation("修改商品")
    @PostMapping("/update")
    public CommonResponse updateProduct(@Valid @RequestBody UpdateProductReq productReq) {
        Product product = new Product();
        BeanUtils.copyProperties(productReq, product);
        productService.updateProduct(product);
        return CommonResponse.success();
    }

    @ApiOperation("删除商品")
    @GetMapping("/delete")
    public CommonResponse deleteProduct(@RequestParam Integer id) {
        productService.deleteProduct(id);
        return CommonResponse.success();
    }

    @ApiOperation("批量上下架商品")
    @GetMapping("/batchUpdateSellStatus")
    public CommonResponse batchUpdateSellStatus(@RequestParam Integer[] ids, @RequestParam Integer sellStatus) {
        productService.batchUpdateSellStatus(ids, sellStatus);
        return CommonResponse.success();
    }


    @ApiOperation("后台商品查询")
    @GetMapping("/select")
    public CommonResponse queryProduct(@RequestParam Integer page, @RequestParam Integer pageSize) {
        return CommonResponse.success(productService.listForAdmin(page, pageSize));
    }


}
