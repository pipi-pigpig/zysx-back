package com.nurturing.Controller;

import com.nurturing.Service.ChildParentService;
import com.nurturing.vo.AddParentRequest;
import com.nurturing.vo.ParentInfoVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/child/{childId}/parents")
@CrossOrigin
public class ChildParentController {

    @Autowired
    private ChildParentService childParentService;

    @GetMapping
    public ResponseEntity<List<ParentInfoVO>> getParents(@PathVariable Long childId) {
        List<ParentInfoVO> parents = childParentService.getParents(childId);
        return ResponseEntity.ok(parents);
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> addParent(
            @PathVariable Long childId,
            @RequestBody AddParentRequest request) {

        try {
            ParentInfoVO parent = childParentService.addParent(childId, request);

            Map<String, Object> response = new HashMap<>();
            response.put("code", 1);
            response.put("msg", "添加成功");
            response.put("data", parent);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("code", 0);
            response.put("msg", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @PutMapping("/{parentId}")
    public ResponseEntity<Map<String, Object>> updateParent(
            @PathVariable Long childId,
            @PathVariable Long parentId,
            @RequestBody AddParentRequest request) {

        try {
            ParentInfoVO parent = childParentService.updateParent(childId, parentId, request);

            Map<String, Object> response = new HashMap<>();
            response.put("code", 1);
            response.put("msg", "更新成功");
            response.put("data", parent);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("code", 0);
            response.put("msg", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @DeleteMapping("/{parentId}")
    public ResponseEntity<Map<String, Object>> deleteParent(
            @PathVariable Long childId,
            @PathVariable Long parentId) {

        try {
            boolean success = childParentService.deleteParent(childId, parentId);

            Map<String, Object> response = new HashMap<>();
            if (success) {
                response.put("code", 1);
                response.put("msg", "删除成功");
                return ResponseEntity.ok(response);
            } else {
                response.put("code", 0);
                response.put("msg", "删除失败");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            }
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("code", 0);
            response.put("msg", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
}