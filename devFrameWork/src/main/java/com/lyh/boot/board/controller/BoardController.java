package com.lyh.boot.board.controller;

import java.io.File;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import com.lyh.boot.board.domain.BoardVO;
import com.lyh.boot.board.domain.FileVO;
import com.lyh.boot.board.service.BoardService;
 
@Controller
public class BoardController {
 
    @Resource(name="com.lyh.boot.board.service.BoardService")
    BoardService mBoardService;
    
    @RequestMapping("/board/list") //게시판 리스트 화면 호출  
    private String boardList(Model model) throws Exception{
        model.addAttribute("list", mBoardService.boardListService());
        return "board/list"; //생성할 jsp
    }
    
    @RequestMapping("/board/detail/{bno}") 
    private String boardDetail(@PathVariable int bno, Model model) throws Exception{
        model.addAttribute("detail", mBoardService.boardDetailService(bno));
        return "/board/detail";
    }
    
    @RequestMapping("/board/insert") //게시글 작성폼 호출  
    private String boardInsertForm(){
        return "/board/insert";
    }
    
    @RequestMapping("/board/insertProc")
    private String boardInsertProc(HttpServletRequest request, @RequestPart MultipartFile files) throws Exception{
        
        BoardVO board = new BoardVO();
        FileVO  file  = new FileVO();
        
        board.setSubject(request.getParameter("subject"));
        board.setContent(request.getParameter("content"));
        board.setWriter(request.getParameter("writer"));
        
        
        if(files.isEmpty()){ //업로드할 파일이 없을 시
            mBoardService.boardInsertService(board); //게시글 insert
        }else{
            String fileName = files.getOriginalFilename(); 
            String fileNameExtension = FilenameUtils.getExtension(fileName).toLowerCase(); 
            File destinationFile; 
            String destinationFileName; 
            String fileUrl = "c:/DEV/sts-bundle/workspace/demo/src/main/webapp/uploadfiles/";
            
            do { 
                destinationFileName = RandomStringUtils.randomAlphanumeric(32) + "." + fileNameExtension; 
                destinationFile = new File(fileUrl+ destinationFileName); 
            } while (destinationFile.exists()); 
            
            destinationFile.getParentFile().mkdirs(); 
            files.transferTo(destinationFile); 
            
            mBoardService.boardInsertService(board); //게시글 insert
            
            file.setBno(board.getBno());
            System.out.println(board.getBno());
            System.out.println(destinationFileName);
            System.out.println(fileName);
            System.out.println(fileUrl);
            
            file.setFileName(destinationFileName);
            
             file.setFileOriName(fileName);
            file.setFileUrl(fileUrl);
            
            mBoardService.fileInsertService(file); //file insert
        }
        
        
        return "redirect:/board/list";
    }

    @RequestMapping("/board/update/{bno}") //게시글 수정폼 호출  
    private String boardUpdateForm(@PathVariable int bno, Model model) throws Exception{
        model.addAttribute("detail", mBoardService.boardDetailService(bno));
        return "/board/update";
    }
    
    @RequestMapping("/board/updateProc")
    private String boardUpdateProc(HttpServletRequest request) throws Exception{
        
        BoardVO board = new BoardVO();
        board.setSubject(request.getParameter("subject"));
        board.setContent(request.getParameter("content"));
        board.setBno(Integer.parseInt(request.getParameter("bno")));
        
        mBoardService.boardUpdateService(board);
        
        return "redirect:/board/detail/"+request.getParameter("bno"); 
    }
 
    @RequestMapping("/board/delete/{bno}")
    private String boardDelete(@PathVariable int bno) throws Exception{
        mBoardService.boardDeleteService(bno);
        return "redirect:/board/list";
    }
    
}