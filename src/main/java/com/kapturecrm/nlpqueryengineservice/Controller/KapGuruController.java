package com.kapturecrm.nlpqueryengineservice.Controller;



import com.kapturecrm.nlpqueryengineservice.service.KapGuruService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController("/api/v1/kapGuru")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class KapGuruController {
    private final KapGuruService kapGuruService;

    @GetMapping("get-data-from-nlp")
    public ResponseEntity<?> getDataFromNlp(@RequestParam String text) {
        return kapGuruService.getDataFromNlp(text);
    }
}
