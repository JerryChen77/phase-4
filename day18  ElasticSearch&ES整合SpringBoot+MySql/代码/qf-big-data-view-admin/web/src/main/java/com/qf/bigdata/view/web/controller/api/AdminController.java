package com.qf.bigdata.view.web.controller.api;


import com.qf.bigdata.view.web.service.AdminService;
import com.qf.data.view.core.model.request.SearchRequest;
import com.qf.data.view.core.model.result.ResultModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;


    @RequestMapping("/search")
    public ResultModel searchDevice(@RequestBody SearchRequest request){
        return adminService.searchDevice(request);




    }

}
