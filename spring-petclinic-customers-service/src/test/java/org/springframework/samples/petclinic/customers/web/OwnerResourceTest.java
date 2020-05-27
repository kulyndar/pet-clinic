package org.springframework.samples.petclinic.customers.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.internal.cglib.core.$CodeGenerationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.samples.petclinic.customers.model.Owner;
import org.springframework.samples.petclinic.customers.model.OwnerRepository;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@ExtendWith(SpringExtension.class)
@WebMvcTest(OwnerResource.class)
@ActiveProfiles("test")
class OwnerResourceTest {
    private static final int OWNER_COUNT = 3;
    private static final String[] names = new String[]{"Ben", "Charles", "Wendy"};
    private static final String[] surnames = new String[]{"Johnson", "Ericson", "Been"};
    private static final String[] addresses = new String[]{"Technicka 2", "1. Avenue 12", "Karlovo namesti 16"};
    private static final String[] cities = new String[]{"Prague", "New York", "Prague"};
    private static final String[] phones = new String[]{"1234567891", "3216549871", "1234567891"};
    private List<Owner> owners = new ArrayList<>();

    @Autowired
    MockMvc mvc;

    @MockBean
    OwnerRepository ownerRepository;

    @BeforeEach
    void setUp() {
        owners = new ArrayList<>();
        for (int i = 0; i < OWNER_COUNT; i++) {
            owners.add(setupOwner(i));
        }
    }

    @Test
    void createOwner() throws Exception {
        Owner owner = owners.get(0);
        given(ownerRepository.save(owner)).willReturn(owner);

        mvc.perform(post("/owners").content(asJsonString(owner))
            .contentType("application/json").accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated());
    }


    @Test
    void findOwner() throws Exception {
        Owner owner = owners.get(0);
        given(ownerRepository.findById(1)).willReturn(Optional.of(owner));

        mvc.perform(get("/owners/1").contentType("application/json").accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.firstName").value(owner.getFirstName()))
            .andExpect(jsonPath("$.lastName").value(owner.getLastName()))
            .andExpect(jsonPath("$.address").value(owner.getAddress()))
            .andExpect(jsonPath("$.city").value(owner.getCity()))
            .andExpect(jsonPath("$.telephone").value(owner.getTelephone()));
    }

    @Test
    void updateOwner() throws Exception {
        Owner owner = owners.get(1);
        Owner newData = owners.get(0);
        given(ownerRepository.findById(1)).willReturn(Optional.of(owner));

        mvc.perform(put("/owners/1").contentType("application/json").content(asJsonString(newData)).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        mvc.perform(get("/owners/1").contentType("application/json").accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.firstName").value(newData.getFirstName()))
            .andExpect(jsonPath("$.lastName").value(newData.getLastName()))
            .andExpect(jsonPath("$.address").value(newData.getAddress()))
            .andExpect(jsonPath("$.city").value(newData.getCity()))
            .andExpect(jsonPath("$.telephone").value(newData.getTelephone()));
    }

    private Owner setupOwner(int idx) {
        Owner owner = new Owner();
        owner.setFirstName(names[idx % OWNER_COUNT]);
        owner.setLastName(surnames[idx % OWNER_COUNT]);
        owner.setAddress(addresses[idx % OWNER_COUNT]);
        owner.setCity(cities[idx % OWNER_COUNT]);
        owner.setTelephone(phones[idx % OWNER_COUNT]);
        return owner;
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
