package com.sid.gl.elections;


import com.sid.gl.commons.AbstractController;
import com.sid.gl.commons.ApiConstants;
import com.sid.gl.commons.ApiResponse;
import com.sid.gl.exceptions.ElectionAlreadyExistException;
import com.sid.gl.exceptions.ElectionNotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = ApiConstants.BASE_PATH+"/electeur")

public class ElectionController extends AbstractController {
    private final ElectionService electionService;

    public ElectionController( ElectionService electionService) {
        this.electionService = electionService;
    }

    @Operation(summary = "creation d'un election")
    @PostMapping( "/create")
    public ResponseEntity<ApiResponse> createElection(@RequestBody @Valid ElectionRequestDto request) throws ElectionAlreadyExistException {
        return getResponseEntity(electionService.createElection(request));
    }

    @Operation(summary = "Recuperation la list des elections")
    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllElection(
            @RequestParam( name = "page", defaultValue = ApiConstants.SIZE)int page,
    ){
        return getResponseEntity(electionService.getAllElections());
    }
    @Operation(summary = "Recuperation d'un election depuis l'id")
    @GetMapping("get/{id}")
    public ResponseEntity <ApiResponse> getElectionById(@PathVariable (name = "id") Long id) throws ElectionNotFoundException {
        return getResponseEntity(electionService.getElection(id));
    }

    @Operation(summary = "Recuperation des elections active")
    @GetMapping("get/active")
    public ResponseEntity<ApiResponse> getAllElectionsByElectionId(){
        return getResponseEntity(electionService.getElectionIsActive());
    }
}
