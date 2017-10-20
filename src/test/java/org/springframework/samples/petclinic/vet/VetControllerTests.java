package org.springframework.samples.petclinic.vet;

import static org.hamcrest.xml.HasXPath.hasXPath;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.samples.petclinic.vet.Specialty;
import org.springframework.samples.petclinic.vet.Vet;
import org.springframework.samples.petclinic.vet.VetController;
import org.springframework.samples.petclinic.vet.VetRepository;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;

/**
 * Test class for the {@link VetController}
 */
@RunWith(SpringRunner.class)
@WebMvcTest(VetController.class)
public class VetControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private VetRepository vets;

    @MockBean
    private SpecialityRepository specialityRepository;

    @Before
    public void setup() {
        Vet james = new Vet();
        james.setFirstName("James");
        james.setLastName("Carter");
        james.setId(1);
        Vet helen = new Vet();
        helen.setFirstName("Helen");
        helen.setLastName("Leary");
        helen.setId(2);

        Specialty radiology = new Specialty();
        radiology.setId(1);
        radiology.setName("radiology");

        helen.addSpecialty(radiology);

        Specialty  surgery = new Specialty();
        surgery.setId(2);
        surgery.setName("surgery");


        Specialty  dentistry = new Specialty();
        dentistry.setId(3);
        dentistry.setName("dentistry");

        List<Specialty> specialtyList = new ArrayList<>();
        specialtyList.add(radiology);
        specialtyList.add(surgery);
        specialtyList.add(dentistry);

        given(this.vets.findAll()).willReturn(Lists.newArrayList(james, helen));
        given(this.specialityRepository.findAll()).willReturn(specialtyList);
    }

    @Test
    public void testShowVetListHtml() throws Exception {
        mockMvc.perform(get("/vets.html"))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("vets"))
            .andExpect(view().name("vets/vetList"));
    }

    @Test
    public void testShowResourcesVetList() throws Exception {
        ResultActions actions = mockMvc.perform(get("/vets.json").accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
        actions.andExpect(content().contentType("application/json;charset=UTF-8"))
            .andExpect(jsonPath("$.vetList[0].id").value(1));
    }

    @Test
    public void testShowVetListXml() throws Exception {
        mockMvc.perform(get("/vets.xml").accept(MediaType.APPLICATION_XML))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_XML_VALUE))
            .andExpect(content().node(hasXPath("/vets/vetList[id=1]/id")));
    }

    @Test
    public void testInitCreationForm() throws Exception {
        mockMvc.perform(get("/vets/new"))
            .andExpect(status().isOk())
            .andExpect(view().name("vets/createOrUpdateVetForm"))
            .andExpect(model().attributeExists("vet"));
    }

    @Test
    public void testProcessCreationFormSuccess() throws Exception {
        mockMvc.perform(post("/vets/new")
            .param("firstName", "TestFirstName")
            .param("lastName", "TestLastName")
            .param("specialties", "radiology")
        )
            .andExpect(status().is3xxRedirection())
            .andExpect(view().name("redirect:/vets/vetList"));
    }

    @Test
    public void testProcessCreationFormError() throws Exception {
        mockMvc.perform(post("/vets/new")
            .param("firstName", "TestFirstName")
            .param("lastName", "TestLastName")
        )
            .andExpect(status().is2xxSuccessful())
            .andExpect(view().name("vets/createOrUpdateVetForm"));
    }

}
