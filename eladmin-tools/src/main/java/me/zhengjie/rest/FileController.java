package me.zhengjie.rest;

import me.zhengjie.aop.log.Log;
import me.zhengjie.domain.FileInfo;
import me.zhengjie.service.PictureService;
import me.zhengjie.service.dto.PictureQueryCriteria;
import me.zhengjie.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 郑杰
 * @date 2018/09/20 14:13:32
 */
@RestController
@RequestMapping("/api")
public class FileController {

    @Autowired
    private PictureService pictureService;

    @Log("查询文件")
    @PreAuthorize("hasAnyRole('ADMIN','PICTURE_ALL','PICTURE_SELECT')")
    @GetMapping(value = "/file")
    public ResponseEntity getRoles(PictureQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity(pictureService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    /**
     * 上传图片
     * @param file
     * @return
     * @throws Exception
     */
    @Log("上传文件")
    @PreAuthorize("hasAnyRole('ADMIN','PICTURE_ALL','PICTURE_UPLOAD')")
    @PostMapping(value = "/file")
    public ResponseEntity upload(@RequestParam MultipartFile file){
        String userName = SecurityUtils.getUsername();
        FileInfo picture = pictureService.upload(file,userName);
        Map map = new HashMap();
        map.put("errno",0);
        map.put("id",picture.getId());
        map.put("data",new String[]{picture.getUrl()});
        return new ResponseEntity(map,HttpStatus.OK);
    }

    /**
     * 删除图片
     * @param id
     * @return
     */
    @Log("删除文件")
    @PreAuthorize("hasAnyRole('ADMIN','PICTURE_ALL','PICTURE_DELETE')")
    @DeleteMapping(value = "/file/{id}")
    public ResponseEntity delete(@PathVariable Long id) {
        pictureService.delete(pictureService.findById(id));
        return new ResponseEntity(HttpStatus.OK);
    }

    /**
     * 删除多张图片
     * @param ids
     * @return
     */
    @Log("删除文件")
    @PreAuthorize("hasAnyRole('ADMIN','PICTURE_ALL','PICTURE_DELETE')")
    @DeleteMapping(value = "/file")
    public ResponseEntity deleteAll(@RequestBody Long[] ids) {
        pictureService.deleteAll(ids);
        return new ResponseEntity(HttpStatus.OK);
    }
}
