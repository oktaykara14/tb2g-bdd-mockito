package guru.springframework.sfgpetclinic.services.springdatajpa;

import guru.springframework.sfgpetclinic.model.Speciality;
import guru.springframework.sfgpetclinic.repositories.SpecialtyRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SpecialitySDJpaServiceTest {

    @Mock(strictness = Mock.Strictness.LENIENT)
    SpecialtyRepository specialtyRepository;

    @InjectMocks
    SpecialitySDJpaService service;

    @Test
    void testDeleteByObject() {
        //given
        Speciality speciality = new Speciality();

        //when
        service.delete(speciality);

        //then
        then(specialtyRepository).should().delete(any(Speciality.class));
    }

    @Test
    void findByIdTest() {
        //given
        Speciality speciality = new Speciality();
        given(specialtyRepository.findById(1L)).willReturn(Optional.of(speciality));

        //when
        Speciality foundSpecialty = service.findById(1L);

        //then
        assertThat(foundSpecialty).isNotNull();
        then(specialtyRepository).should(timeout(100)).findById(anyLong());
        then(specialtyRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    void deleteById() {
        // given - none

        //when
        service.deleteById(1L);
        service.deleteById(1L);

        //then
        then(specialtyRepository).should(timeout(100).times(2)).deleteById(1L);
    }

    @Test
    void deleteByIdAtLeast() {
        //given - none

        //when
        service.deleteById(1L);
        service.deleteById(1L);

        //then
        then(specialtyRepository).should(timeout(10).atLeastOnce()).deleteById(1L);
    }

    @Test
    void deleteByIdAtMost() {
        // given - none

        //when
        service.deleteById(1L);
        service.deleteById(1L);

        //then
        then(specialtyRepository).should(atMost(5)).deleteById(1L);
    }

    @Test
    void deleteByIdNever() {
        // given - none

        //when
        service.deleteById(1L);
        service.deleteById(1L);

        //then
        then(specialtyRepository).should(timeout(200).atLeastOnce()).deleteById(1L);
        then(specialtyRepository).should(never()).deleteById(5L);
    }

    @Test
    void testDelete() {
        // given - none

        //when
        service.delete(new Speciality());

        //then
        then(specialtyRepository).should().delete(any());
    }

    @Test
    void testDoThrow() {
        doThrow(new RuntimeException("boom")).when(specialtyRepository).delete(any());

        assertThrows(RuntimeException.class, ()-> specialtyRepository.delete(new Speciality()));

        verify(specialtyRepository).delete(any());
    }

    @Test
    void testFindByIDThrows() {
        given(specialtyRepository.findById(1L)).willThrow(new RuntimeException("boom"));

        assertThrows(RuntimeException.class, () -> service.findById(1L));

        then(specialtyRepository).should().findById(1L);
    }

    @Test
    void testDeleteBDD() {
        willThrow(new RuntimeException("boom")).given(specialtyRepository).delete(any());

        assertThrows(RuntimeException.class, () -> specialtyRepository.delete(new Speciality()));

        then(specialtyRepository).should().delete(any());
    }

    @Test
    void testSaveLambda() {
        //given
        final String MATCH_ME = "MATCH_ME";
        Speciality speciality = new Speciality();
        speciality.setDescription(MATCH_ME);

        Speciality savedSpecialty = new Speciality();
        savedSpecialty.setId(1L);

        //need mock to only return on match MATCH_ME string
        given(specialtyRepository.save(argThat(argument -> argument.getDescription().equals(MATCH_ME)))).willReturn(savedSpecialty);

        //when
        Speciality returnedSpecialty = service.save(speciality);

        //then
        assertThat(returnedSpecialty.getId()).isEqualTo(1L);
    }

    @Test
    void testSaveLambdaNoMatch() {
        //given
        final String MATCH_ME = "MATCH_ME";
        Speciality speciality = new Speciality();
        speciality.setDescription("Not a match");

        Speciality savedSpecialty = new Speciality();
        savedSpecialty.setId(1L);

        //need mock to only return on match MATCH_ME string
        given(specialtyRepository.save(argThat(argument -> argument.getDescription().equals(MATCH_ME)))).willReturn(savedSpecialty);

        //when
        Speciality returnedSpecialty = service.save(speciality);

        //then
        assertNull(returnedSpecialty);
    }
}