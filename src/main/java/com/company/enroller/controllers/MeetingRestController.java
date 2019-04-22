package com.company.enroller.controllers;

import com.company.enroller.model.Meeting;
import com.company.enroller.model.Participant;
import com.company.enroller.persistence.MeetingService;
import com.company.enroller.persistence.ParticipantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/meetings")

public class MeetingRestController {
    @Autowired
    MeetingService meetingService;
    ParticipantService participantService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity<?> getMeetings() {
        Collection<Meeting> meetings = meetingService.getAll();
        return new ResponseEntity<Collection<Meeting>>(meetings, HttpStatus.OK);
    }

    @RequestMapping(value = "/{Id}", method = RequestMethod.GET)
    public ResponseEntity<?> getMeetingById(@PathVariable("Id") long Id) {
        Meeting meeting = meetingService.getMeetingById(Id);
        if (meeting == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Meeting>(meeting, HttpStatus.OK);
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseEntity<?> addMeeting(@RequestBody Meeting meeting) {
        Meeting foundMeeting = meetingService.getMeetingById(meeting.getId());
        if (foundMeeting != null) {
            return new ResponseEntity("Unable to create. A meeting with Id " + meeting.getId() + " already exist.", HttpStatus.CONFLICT);
        }
        meetingService.add(meeting);
        return new ResponseEntity<Meeting>(meeting, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/{Id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteMeeting(@PathVariable("Id") long Id) {
        Meeting meeting = meetingService.getMeetingById(Id);
        if (meeting == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        meetingService.delete(meeting);
        return new ResponseEntity<Meeting>(meeting, HttpStatus.OK);
    }

    @RequestMapping(value = "/{Id}", method = RequestMethod.PUT)
    public ResponseEntity<?> updateMeeting(@PathVariable("Id") long Id,
                                               @RequestBody Meeting meetingFromFrontend) {
        Meeting meeting = meetingService.getMeetingById(Id);
        if (meeting == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        meeting.setId(meetingFromFrontend.getId());
        meeting.setDate(meetingFromFrontend.getDate());
        meeting.setDescription(meetingFromFrontend.getDescription());
        meeting.setTitle(meetingFromFrontend.getTitle());
        meetingService.update(meeting);
        return new ResponseEntity<Meeting>(meeting, HttpStatus.OK);
    }

    @RequestMapping(value = "/{Id}", method = RequestMethod.PATCH)
    public ResponseEntity<?> addParticipantToMeeting(@PathVariable("Id") long Id, @RequestBody String participantLogin) {
        Meeting meeting = meetingService.getMeetingById(Id);
        Participant participant = participantService.findByLogin(participantLogin);
        if (meeting == null || participant == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        meeting.addParticipant(participant);

        meetingService.update(meeting);
        return new ResponseEntity<Meeting>(meeting, HttpStatus.OK);
    }

}
