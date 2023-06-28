package com.challenge.lendtech.controller;

import com.challenge.lendtech.entity.ElevatorHandler;
import com.challenge.lendtech.entity.ElevatorServer;
import com.challenge.lendtech.entity.Elevator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/elevator")
public class ElevatorController {
    @Value("${elevator-num-of-floors:15}")
    private int max_no_of_floors;
    @PostMapping("/callElevator")
    public ResponseEntity<String> callElevator(@RequestParam int toFloor) {
        if(toFloor > max_no_of_floors ){
            return ResponseEntity.ok("Sorry there is no " + toFloor + " the maximum floor number is " + max_no_of_floors);
        }
        // Start the elevator server
        ElevatorServer server = new ElevatorServer() ;
        new Thread(server).start() ;
        // Get the instance of Elevator
        Elevator elevator = Elevator.getElevator();
        ElevatorHandler.goToFloor(elevator, toFloor);
        return ResponseEntity.ok("Elevator called floor " + toFloor);
    }
}
