package com.sid.gl.crons;

import com.sid.gl.elections.Election;
import com.sid.gl.elections.ElectionRepository;
import com.sid.gl.notifications.NotificationService;
import com.sid.gl.users.Role;
import com.sid.gl.users.User;
import com.sid.gl.users.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class ElectionScheduleTaskTest {

    @Mock
    private ElectionRepository electionRepository;
    @Mock
    private NotificationService notificationService;
    @Mock
    private UserRepository userRepository;

    @Captor
    private ArgumentCaptor<Election> electionCaptor;
    @Captor
    private ArgumentCaptor<String> emailCaptor;
    @Captor
    private ArgumentCaptor<String> subjectCaptor;
    @Captor
    private ArgumentCaptor<String> bodyCaptor;

    @InjectMocks
    private ElectionScheduleTask electionScheduleTask;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void processOpenElectionAndNotify_shouldActivateElectionsAndNotifyElecteurs() {
        //GIVEN

        Election election = new Election();
        election.setName("Test Election");
        election.setActive(false);

        User user = new User();
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("john.doe@example.com");
        Role role = new Role();
        role.setRoleName("ELECTOR");
        user.setRoles(Set.of(role));

        //WHEN

        when(electionRepository.findByStartDate()).thenReturn(List.of(election));
        when(userRepository.findByRoles_RoleName("ELECTOR")).thenReturn(List.of(user));

        //THEN

        electionScheduleTask.processOpenElectionAndNotify();

        //verify
        verify(electionRepository).save(argThat(e -> e.isActive())); //verify that the election is activated
        verify(notificationService).sendEmail(eq("john.doe@example.com"), anyString(), contains("Test Election"));
    }

    @Test
    void processOpenElectionAndNotify_shouldCaptureArguments() {
        Election election = new Election();
        election.setName("Test Election");
        election.setActive(false);

        User user = new User();
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("john.doe@example.com");
        Role role = new Role();
        role.setRoleName("ELECTOR");
        user.setRoles(Set.of(role));

        when(electionRepository.findByStartDate()).thenReturn(List.of(election));
        when(userRepository.findByRoles_RoleName("ELECTOR")).thenReturn(List.of(user));

        electionScheduleTask.processOpenElectionAndNotify();

        verify(electionRepository).save(electionCaptor.capture());
        Election savedElection = electionCaptor.getValue();
        assertTrue(savedElection.isActive());

        verify(notificationService).sendEmail(
                emailCaptor.capture(),
                subjectCaptor.capture(),
                bodyCaptor.capture()
        );
        assertEquals("john.doe@example.com", emailCaptor.getValue());
        assertTrue(subjectCaptor.getValue().contains("Notification d'ouverture"));
        assertTrue(bodyCaptor.getValue().contains("Test Election"));
    }


    @Test
    void processOpenElectionAndNotify_shouldHandleNoElectionsGracefully() {
        when(electionRepository.findByStartDate()).thenReturn(Collections.emptyList());
        when(userRepository.findByRoles_RoleName("ELECTOR")).thenReturn(Collections.emptyList());

        electionScheduleTask.processOpenElectionAndNotify();

        verify(electionRepository, never()).save(any());
        verify(notificationService, never()).sendEmail(anyString(), anyString(), anyString());
    }
}
