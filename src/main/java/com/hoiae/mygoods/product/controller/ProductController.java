package com.hoiae.mygoods.product.controller;
import com.hoiae.mygoods.product.dto.CharacterDTO;
import com.hoiae.mygoods.product.dto.ProductDTO;
import com.hoiae.mygoods.product.service.ProductService;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;


import java.io.*;
import java.util.*;

@Controller
@RequestMapping("/product")
public class ProductController {

    private final ProductService productService;

    @Value("${image.image-dir}")
    private String IMAGE_DIR;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("")
    public  String product() {
        return "content/product/index";
    }


    @ResponseBody
    @PostMapping("/character")
    public String uploadCharacter(@RequestParam("image") MultipartFile image) throws ParseException {


        System.out.println("image : " + image.getOriginalFilename());

        //RestTemplate을 이용한 단일 파일 업로드

        /*  create RestTemplate instance*/
        RestTemplate restTemplate = new RestTemplate();

        /*request header*/
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);


        /*request body*/
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<String, Object>();
        System.out.println(image.getResource());
        body.add("file", image.getResource());

        /*헤더와 본문 개체를 감싸는 HttpEntity 인스턴스를 생성하고 RestTemplate을 사용하여 게시한다.*/
        HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<MultiValueMap<String, Object>>(body, headers);

        /*restTemplate.postForEntity() 호출은 주어진 URL에 연결하고 파일을 서버로 보내는 작업을 완료*/

        String url  = "https://7d663fa4-f5b9-47a2-98b1-7cbe746422fd.mock.pstmn.io/product";


        ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);

        JSONParser parser = new JSONParser();
        Object obj = parser.parse(response.getBody());
        JSONObject jsonObj = (JSONObject) obj;
        System.out.println("json['result'] : " + jsonObj.get("result"));
        System.out.println("json['img'] : " + jsonObj.get("img"));
        System.out.println("getbody" + response.getBody());

        CharacterDTO character  = new CharacterDTO();
        character.setMemberNo(1);
        character.setModelName("테스트모델");
        character.setCharacterImageUrl("test.png");


        productService.registCharacter(character);

        return response.getBody();
    }

    @ResponseBody
    @PostMapping("/upload")
    public String uploadImage(@RequestParam("image") MultipartFile userImage,
                              @RequestParam("categoryCode") String categoryCode,
                              @RequestParam("modelName") String modelName
                              ) throws IOException, ParseException {

        /*원본파일명, 카테고리코드, 모델코드 출력*/
        System.out.println("file : " + userImage.getOriginalFilename());
        System.out.println("categoryCode : " + categoryCode);
        System.out.println("modelName : " + modelName);

        /* 파일업로드*/

        String rootLocation = IMAGE_DIR;

        String fileUploadDirectory = rootLocation +"/upload/original";
        File directory = new File(fileUploadDirectory);

        if(!directory.exists()){
            directory.mkdirs();
        }

        List<Map<String, String>> fileList = new ArrayList<>();

        List<MultipartFile> paramFileList = new ArrayList<>();
        paramFileList.add(userImage);

        try {
            for (MultipartFile paramFile : paramFileList) {
                if (paramFile.getSize() > 0) {
                    String originFileName = paramFile.getOriginalFilename();

                    String ext = originFileName.substring(originFileName.lastIndexOf("."));
                    String savedFileName = UUID.randomUUID().toString().replace("-", "") + ext;


                    paramFile.transferTo(new File(fileUploadDirectory + "/" + savedFileName));

                    /* DB에 업로드한 파일의 정보를 저장하는 비지니스 로직 수행 */
                    /* 필요한 정보를 Map에 담는다. */
                    Map<String, String> fileMap = new HashMap<>();
                    fileMap.put("originFileName", originFileName);
                    fileMap.put("savedFileName", savedFileName);
                    fileMap.put("savePath", fileUploadDirectory);

                    System.out.println("파일경로 " +   fileUploadDirectory + "/" + savedFileName);

                    ProductDTO product = new ProductDTO();
                    product.setProductCode(1);
                    product.setProductName("나만의 에코백");
                    product.setProductPrice(10000);
                    product.setCategoryCode(3);
                    product.setCharachterCode(1);
                    product.setProductImageUrl(savedFileName);

                    productService.registProduct(product);
//
//                    /* 제목 사진과 나머지 사진을 구분하고 썸네일도 생성한다. */
//                    int width = 0;
//                    int height = 0;
//
//                    String fieldName = paramFile.getName();
//
//                    if ("thumbnailImg1".equals(fieldName)) {
//                        fileMap.put("fileType", "TITLE");
//
//                        /* 썸네일로 변환 할 사이즈를 지정한다. */
//                        width = 300;
//                        height = 150;
//                    } else {
//                        fileMap.put("fileType", "BODY");
//
//                        width = 120;
//                        height = 100;
//                    }
//
//                    /* 썸네일로 변환 후 저장한다. */
//                    Thumbnails.of(fileUploadDirectory + "/" + savedFileName).size(width, height)
//                            .toFile(thumbnailDirectory + "/thumbnail_" + savedFileName);
//
//                    /* 나중에 웹서버에서 접근 가능한 경로 형태로 썸네일의 저장 경로도 함께 저장한다. */
//                    fileMap.put("thumbnailPath", "/upload/thumbnail/thumbnail_" + savedFileName);
//
//                    fileList.add(fileMap);
                }
            }


//            /* 서비스를 요청할 수 있도록 BoardDTO에 담는다. */
//
//            thumbnail.setAttachmentList(new ArrayList<AttachmentDTO>());
//            List<AttachmentDTO> list = thumbnail.getAttachmentList();
//            for (int i = 0; i < fileList.size(); i++) {
//                Map<String, String> file = fileList.get(i);
//
//                AttachmentDTO tempFileInfo = new AttachmentDTO();
//                tempFileInfo.setOriginalName(file.get("originFileName"));
//                tempFileInfo.setSavedName(file.get("savedFileName"));
//                tempFileInfo.setSavePath(file.get("savePath"));
//                tempFileInfo.setFileType(file.get("fileType"));
//                tempFileInfo.setThumbnailPath(file.get("thumbnailPath"));
//
//                list.add(tempFileInfo);
//            }
//
//
//            boardService.registThumbnail(thumbnail);
//
//            rttr.addFlashAttribute("message", "사진 게시판 등록에 성공하셨습니다.");

        } catch (IllegalStateException | IOException e) {
            e.printStackTrace();

            /* 어떤 종류의 Exception이 발생 하더라도실패 시 파일을 삭제해야 한다. */
            int cnt = 0;
            for (int i = 0; i < fileList.size(); i++) {
                Map<String, String> file = fileList.get(i);

                File deleteFile = new File(fileUploadDirectory + "/" + file.get("savedFileName"));
                boolean isDeleted1 = deleteFile.delete();

//                File deleteThumbnail = new File(thumbnailDirectory + "/thumbnail_" + file.get("savedFileName"));
//                boolean isDeleted2 = deleteThumbnail.delete();

                if (isDeleted1) {
                    cnt++;
                }
            }

            if (cnt == fileList.size()) {
                e.printStackTrace();
            } else {
                e.printStackTrace();
            }
        }


        //RestTemplate을 이용한 단일 파일 업로드

        /*  create RestTemplate instance*/
        RestTemplate restTemplate = new RestTemplate();

        /*request header*/
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);


        /*request body*/
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<String, Object>();
        System.out.println(userImage.getResource());
        body.add("file", userImage.getResource());
        body.add("categoryCode",categoryCode);


        /*헤더와 본문 개체를 감싸는 HttpEntity 인스턴스를 생성하고 RestTemplate을 사용하여 게시한다.*/
        HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<MultiValueMap<String, Object>>(body, headers);

        /*restTemplate.postForEntity() 호출은 주어진 URL에 연결하고 파일을 서버로 보내는 작업을 완료*/

        String url  = "https://7d663fa4-f5b9-47a2-98b1-7cbe746422fd.mock.pstmn.io/product";


        ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);

        //tshirt, plate,ecobag


        JSONParser parser = new JSONParser();
        Object obj = parser.parse(response.getBody());
        JSONObject jsonObj = (JSONObject) obj;
        System.out.println("json['result'] : " + jsonObj.get("result"));
        System.out.println("json['img'] : " + jsonObj.get("img"));



        return response.getBody();

    }
    }
