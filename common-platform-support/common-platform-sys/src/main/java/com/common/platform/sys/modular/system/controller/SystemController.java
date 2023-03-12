package com.common.platform.sys.modular.system.controller;

import cn.hutool.core.bean.BeanUtil;
import com.common.platform.auth.context.LoginContextHolder;
import com.common.platform.auth.pojo.LoginUser;
import com.common.platform.base.exception.CoreExceptionEnum;
import com.common.platform.base.exception.ServiceException;
import com.common.platform.sys.base.oshi.SystemHardwareInfo;
import com.common.platform.base.utils.CoreUtil;
import com.common.platform.sys.base.controller.BaseController;
import com.common.platform.sys.exception.RequestEmptyException;
import com.common.platform.sys.factory.ConstantFactory;
import com.common.platform.sys.log.LogObjectHolder;
import com.common.platform.sys.modular.system.entity.User;
import com.common.platform.sys.modular.system.model.UploadResult;
import com.common.platform.sys.modular.system.service.FileInfoService;
import com.common.platform.sys.modular.system.service.UserService;
import com.common.platform.sys.base.controller.response.ResponseData;
import com.common.platform.sys.base.controller.response.SuccessResponseData;
import com.common.platform.sys.util.DefaultImages;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;

/**
 * 通用控制器
 */
@Controller
@RequestMapping("/system")
@Slf4j
public class SystemController extends BaseController {

    @Autowired
    private UserService userService;

    @Autowired
    private FileInfoService fileInfoService;

    /**
     * 系统硬件信息页面
     */
    @RequestMapping("/systemInfo")
    public String systemInfo(Model model) {

        SystemHardwareInfo systemHardwareInfo = new SystemHardwareInfo();
        systemHardwareInfo.copyTo();

        model.addAttribute("server", systemHardwareInfo);
        return "/modular/frame/systemInfo.html";
    }

    /**
     * 主页面
     */
    @RequestMapping("/welcome")
    public String welcome() {
        return "/modular/frame/welcome.html";
    }

    /**
     * 跳转到修改密码界面
     */
    @RequestMapping("/user_chpwd")
    public String chPwd() {
        return "/modular/frame/password.html";
    }

    /**
     * 跳转到查看用户详情页面
     */
    @RequestMapping("/user_info")
    public String userInfo(Model model) {
        Long userId = LoginContextHolder.getContext().getUserId();
        User user = this.userService.getById(userId);

        model.addAllAttributes(BeanUtil.beanToMap(user));
        model.addAttribute("roleName", ConstantFactory.me().getRoleName(user.getRoleId()));
        model.addAttribute("deptName", ConstantFactory.me().getDeptName(user.getDeptId()));
        model.addAttribute("avatar", DefaultImages.defaultAvatarUrl());
        LogObjectHolder.me().set(user);

        return "/modular/frame/user_info.html";
    }

    /**
     * 通用的树列表选择器
     */
    @RequestMapping("/commonTree")
    public String deptTreeList(@RequestParam("formName") String formName,
                               @RequestParam("formId") String formId,
                               @RequestParam("treeUrl") String treeUrl, Model model) {

        if (CoreUtil.isOneEmpty(formName, formId, treeUrl)) {
            throw new RequestEmptyException("请求数据不完整！");
        }

        try {
            model.addAttribute("formName", URLDecoder.decode(formName, "UTF-8"));
            model.addAttribute("formId", URLDecoder.decode(formId, "UTF-8"));
            model.addAttribute("treeUrl", URLDecoder.decode(treeUrl, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            throw new RequestEmptyException("请求数据不完整！");
        }

        return "/common/tree_dlg.html";
    }

    /**
     * 更新头像
     */
    @RequestMapping("/updateAvatar")
    @ResponseBody
    public Object uploadAvatar(@RequestParam("fileId") String fileId) {

        if (CoreUtil.isEmpty(fileId)) {
            throw new RequestEmptyException("请求头像为空");
        }

        fileInfoService.updateAvatar(fileId);

        return SUCCESS_TIP;
    }

    /**
     * 预览头像
     */
    @RequestMapping("/previewAvatar")
    @ResponseBody
    public Object previewAvatar(HttpServletResponse response) {

        //输出图片的文件流
        try {
            response.setContentType("image/jpeg");
            byte[] decode = this.fileInfoService.previewAvatar();
            response.getOutputStream().write(decode);
        } catch (IOException e) {
            throw new ServiceException(CoreExceptionEnum.SERVICE_ERROR);
        }

        return null;
    }

    /**
     * 获取当前用户详情
     */
    @RequestMapping("/currentUserInfo")
    @ResponseBody
    public ResponseData getUserInfo() {

        LoginUser currentUser = LoginContextHolder.getContext().getUser();
        if (currentUser == null) {
            throw new ServiceException(CoreExceptionEnum.NO_CURRENT_USER);
        }

        return new SuccessResponseData(userService.getUserInfo(currentUser.getId()));
    }

    /**
     * layui上传组件 通用文件上传接口
     */
    @RequestMapping(method = RequestMethod.POST, path = "/upload")
    @ResponseBody
    public ResponseData layuiUpload(@RequestPart("file") MultipartFile file) {

        UploadResult uploadResult = this.fileInfoService.uploadFile(file);
        String fileId = uploadResult.getFileId();

        HashMap<String, Object> map = new HashMap<>();
        map.put("fileId", fileId);

        return ResponseData.success(0, "上传成功", map);
    }

}
