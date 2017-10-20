/*
 * Copyright 2002-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.samples.petclinic.vet;

import java.util.Collection;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.owner.Owner;
import org.springframework.samples.petclinic.owner.Pet;
import org.springframework.samples.petclinic.owner.PetType;
import org.springframework.samples.petclinic.owner.PetValidator;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author Juergen Hoeller
 * @author Mark Fisher
 * @author Ken Krebs
 * @author Arjen Poutsma
 * @author Anand Pimple
 */
@Controller
class VetController {

    private final VetRepository vets;
    private final SpecialityRepository specialities;

    private final static String VIEWS_VETS_CREATE_OR_UPDATE_FORM = "vets/createOrUpdateVetForm";


    @Autowired
    public VetController(VetRepository clinicService,SpecialityRepository specialities) {
        this.specialities = specialities;
        this.vets = clinicService;
    }

    @ModelAttribute("specialties")
    public Collection<Specialty> populateSpecialty() {
        return this.specialities.findAll();
    }

    @InitBinder("vet")
    public void initPetBinder(WebDataBinder dataBinder) {
        dataBinder.setValidator(new VetValidator());
    }


    @RequestMapping(value = { "/vets.html" })
    public String showVetList(Map<String, Object> model) {
        // Here we are returning an object of type 'Vets' rather than a collection of Vet
        // objects so it is simpler for Object-Xml mapping
        Vets vets = new Vets();
        vets.getVetList().addAll(this.vets.findAll());
        model.put("vets", vets);
        return "vets/vetList";
    }

    @RequestMapping(value = { "/vets/createOrUpdateVetForm.html" })
    public String addOrUpdateVet(Map<String, Object> model) {

        Vets vets = new Vets();
        vets.getVetList().addAll(this.vets.findAll());
        model.put("vets", vets);
        return "vets/vetList";
    }

    @RequestMapping(value = { "/vets.json", "/vets.xml" })
    public @ResponseBody Vets showResourcesVetList() {
        // Here we are returning an object of type 'Vets' rather than a collection of Vet
        // objects so it is simpler for JSon/Object mapping
        Vets vets = new Vets();
        vets.getVetList().addAll(this.vets.findAll());
        return vets;
    }

    @RequestMapping(value = "/vets/new", method = RequestMethod.GET)
    public String initCreationForm(ModelMap model) {
        Vet vet = new Vet();
        model.put("vet", vet);
        return VIEWS_VETS_CREATE_OR_UPDATE_FORM;
    }

    @RequestMapping(value = "/vets/new", method = RequestMethod.POST)
    public String processCreationForm(@Valid Vet vet, BindingResult result, ModelMap model) {
        if (result.hasErrors()) {
            model.put("vet", vet);
            return VIEWS_VETS_CREATE_OR_UPDATE_FORM;
        } else {
            this.vets.save(vet);
            return "redirect:/vets/vetList";
        }
    }
}
