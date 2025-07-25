package com.opyruso.coh.resource;

import com.opyruso.coh.entity.Character;
import com.opyruso.coh.entity.CharacterDetails;
import com.opyruso.coh.entity.DamageType;
import com.opyruso.coh.entity.DamageTypeDetails;
import com.opyruso.coh.entity.DamageBuffType;
import com.opyruso.coh.entity.DamageBuffTypeDetails;
import com.opyruso.coh.entity.Picto;
import com.opyruso.coh.entity.PictoDetails;
import com.opyruso.coh.entity.Weapon;
import com.opyruso.coh.entity.WeaponDetails;
import com.opyruso.coh.entity.Capacity;
import com.opyruso.coh.entity.CapacityDetails;
import com.opyruso.coh.entity.CapacityType;
import com.opyruso.coh.entity.CapacityTypeDetails;
import com.opyruso.coh.entity.Outfit;
import com.opyruso.coh.entity.OutfitDetails;
import com.opyruso.coh.model.dto.CharacterWithDetails;
import com.opyruso.coh.model.dto.DamageTypeWithDetails;
import com.opyruso.coh.model.dto.DamageBuffTypeWithDetails;
import com.opyruso.coh.model.dto.PictoWithDetails;
import com.opyruso.coh.model.dto.WeaponWithDetails;
import com.opyruso.coh.model.dto.CapacityWithDetails;
import com.opyruso.coh.model.dto.CapacityTypeWithDetails;
import com.opyruso.coh.model.dto.OutfitWithDetails;
import com.opyruso.coh.model.PublicData;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;


@Path("/public/data")
@Produces(MediaType.APPLICATION_JSON)
public class PublicDataResource {

    @Inject
    EntityManager em;

    @GET
    @Path("{lang}")
    @Transactional
    public Response getAll(@PathParam("lang") String lang) {
        PublicData data = new PublicData();

        java.util.Map<String, CharacterDetails> characterDetails = CharacterDetails
                .find("lang", lang)
                .list()
                .stream()
                .map(e -> (CharacterDetails) e)
                .collect(java.util.stream.Collectors.toMap(d -> d.idCharacter, d -> d));
        java.util.List<Character> characterEntities = Character.listAll();
        data.characters = characterEntities.stream().map(c -> {
            CharacterDetails d = (CharacterDetails) characterDetails.get(c.idCharacter);
            CharacterWithDetails dto = new CharacterWithDetails();
            dto.idCharacter = c.idCharacter;
            dto.lang = lang;
            dto.name = d != null ? d.name : "";
            dto.story = d != null ? d.story : "";
            return dto;
        }).toList();

        java.util.Map<String, DamageTypeDetails> damageTypeDetails = DamageTypeDetails
                .find("lang", lang)
                .list()
                .stream()
                .map(e -> (DamageTypeDetails) e)
                .collect(java.util.stream.Collectors.toMap(d -> d.idDamageType, d -> d));
        java.util.List<DamageType> damageTypeEntities = DamageType.listAll();
        data.damageTypes = damageTypeEntities.stream().map(dt -> {
            DamageTypeDetails dd = (DamageTypeDetails) damageTypeDetails.get(dt.idDamageType);
            DamageTypeWithDetails dto = new DamageTypeWithDetails();
            dto.idDamageType = dt.idDamageType;
            dto.lang = lang;
            dto.name = dd != null ? dd.name : "";
            return dto;
        }).toList();

        java.util.Map<String, DamageBuffTypeDetails> damageBuffTypeDetails = DamageBuffTypeDetails
                .find("lang", lang)
                .list()
                .stream()
                .map(e -> (DamageBuffTypeDetails) e)
                .collect(java.util.stream.Collectors.toMap(d -> d.idDamageBuffType, d -> d));
        java.util.List<DamageBuffType> damageBuffTypeEntities = DamageBuffType.listAll();
        data.damageBuffTypes = damageBuffTypeEntities.stream().map(db -> {
            DamageBuffTypeDetails dd = (DamageBuffTypeDetails) damageBuffTypeDetails.get(db.idDamageBuffType);
            DamageBuffTypeWithDetails dto = new DamageBuffTypeWithDetails();
            dto.idDamageBuffType = db.idDamageBuffType;
            dto.lang = lang;
            dto.name = dd != null ? dd.name : "";
            return dto;
        }).toList();

        java.util.Map<String, PictoDetails> pictoDetails = PictoDetails
                .find("lang", lang)
                .list()
                .stream()
                .map(e -> (PictoDetails) e)
                .collect(java.util.stream.Collectors.toMap(d -> d.idPicto, d -> d));
        java.util.List<Picto> pictoEntities = Picto.listAll();
        data.pictos = pictoEntities.stream().map(p -> {
            PictoDetails pd = (PictoDetails) pictoDetails.get(p.idPicto);
            PictoWithDetails dto = new PictoWithDetails();
            dto.idPicto = p.idPicto;
            dto.level = p.level;
            dto.bonusDefense = p.bonusDefense;
            dto.bonusSpeed = p.bonusSpeed;
            dto.bonusCritChance = p.bonusCritChance;
            dto.bonusHealth = p.bonusHealth;
            dto.luminaCost = p.luminaCost;
            dto.lang = lang;
            if (pd != null) {
                dto.name = pd.name;
                dto.region = pd.region;
                dto.descrptionBonusLumina = pd.descrptionBonusLumina;
                dto.unlockDescription = pd.unlockDescription;
            } else {
                dto.name = "";
                dto.region = "";
                dto.descrptionBonusLumina = "";
                dto.unlockDescription = "";
            }
            return dto;
        }).toList();

        java.util.Map<String, WeaponDetails> weaponDetails = WeaponDetails
                .find("lang", lang)
                .list()
                .stream()
                .map(e -> (WeaponDetails) e)
                .collect(java.util.stream.Collectors.toMap(d -> d.idWeapon + "#" + d.idCharacter, d -> d));
        java.util.List<Weapon> weaponEntities = Weapon.listAll();
        data.weapons = weaponEntities.stream().map(w -> {
            WeaponDetails wd = (WeaponDetails) weaponDetails.get(w.idWeapon + "#" + w.idCharacter);
            WeaponWithDetails dto = new WeaponWithDetails();
            dto.idWeapon = w.idWeapon;
            dto.character = w.character != null ? w.character.idCharacter : w.idCharacter;
            dto.damageType = w.damageType != null ? w.damageType.idDamageType : null;
            dto.damageBuffType1 = w.damageBuffType1 != null ? w.damageBuffType1.idDamageBuffType : null;
            dto.damageBuffType2 = w.damageBuffType2 != null ? w.damageBuffType2.idDamageBuffType : null;
            dto.lang = lang;
            if (wd != null) {
                dto.name = wd.name;
                dto.region = wd.region;
                dto.unlockDescription = wd.unlockDescription;
                dto.weaponEffect1 = wd.weaponEffect1;
                dto.weaponEffect2 = wd.weaponEffect2;
                dto.weaponEffect3 = wd.weaponEffect3;
            } else {
                dto.name = "";
                dto.region = "";
                dto.unlockDescription = "";
                dto.weaponEffect1 = "";
                dto.weaponEffect2 = "";
                dto.weaponEffect3 = "";
            }
            return dto;
        }).toList();

        java.util.Map<String, OutfitDetails> outfitDetails = OutfitDetails
                .find("lang", lang)
                .list()
                .stream()
                .map(e -> (OutfitDetails) e)
                .collect(java.util.stream.Collectors.toMap(d -> d.idOutfit, d -> d));
        java.util.List<Outfit> outfitEntities = Outfit.listAll();
        data.outfits = outfitEntities.stream().map(o -> {
            OutfitDetails od = (OutfitDetails) outfitDetails.get(o.idOutfit);
            OutfitWithDetails dto = new OutfitWithDetails();
            dto.idOutfit = o.idOutfit;
            dto.character = o.character != null ? o.character.idCharacter : null;
            dto.lang = lang;
            dto.name = od != null ? od.name : "";
            dto.description = od != null ? od.description : "";
            return dto;
        }).toList();

        java.util.Map<String, CapacityDetails> capacityDetails = CapacityDetails
                .find("lang", lang)
                .list()
                .stream()
                .map(e -> (CapacityDetails) e)
                .collect(java.util.stream.Collectors.toMap(d -> d.idCapacity, d -> d));
        java.util.List<Capacity> capacityEntities = Capacity.listAll();
        data.capacities = capacityEntities.stream().map(c -> {
            CapacityDetails cd = (CapacityDetails) capacityDetails.get(c.idCapacity);
            CapacityWithDetails dto = new CapacityWithDetails();
            dto.idCapacity = c.idCapacity;
            dto.character = c.character != null ? c.character.idCharacter : null;
            dto.energyCost = c.energyCost;
            dto.canBreak = c.canBreak;
            dto.damageType = c.damageType != null ? c.damageType.idDamageType : null;
            dto.type = c.type != null ? c.type.idCapacityType : null;
            dto.isMultiTarget = c.isMultiTarget;
            dto.gridPositionX = c.gridPositionX;
            dto.gridPositionY = c.gridPositionY;
            dto.lang = lang;
            if (cd != null) {
                dto.name = cd.name;
                dto.effectPrimary = cd.effectPrimary;
                dto.effectSecondary = cd.effectSecondary;
                dto.bonusDescription = cd.bonusDescription;
                dto.additionnalDescription = cd.additionnalDescription;
            } else {
                dto.name = "";
                dto.effectPrimary = "";
                dto.effectSecondary = "";
                dto.bonusDescription = "";
                dto.additionnalDescription = "";
            }
            return dto;
        }).toList();

        java.util.Map<String, CapacityTypeDetails> capacityTypeDetails = CapacityTypeDetails
                .find("lang", lang)
                .list()
                .stream()
                .map(e -> (CapacityTypeDetails) e)
                .collect(java.util.stream.Collectors.toMap(d -> d.idCapacityType, d -> d));
        java.util.List<CapacityType> capacityTypeEntities = CapacityType.listAll();
        data.capacityTypes = capacityTypeEntities.stream().map(ct -> {
            CapacityTypeDetails ctd = (CapacityTypeDetails) capacityTypeDetails.get(ct.idCapacityType);
            CapacityTypeWithDetails dto = new CapacityTypeWithDetails();
            dto.idCapacityType = ct.idCapacityType;
            dto.lang = lang;
            dto.name = ctd != null ? ctd.name : "";
            return dto;
        }).toList();

        ObjectMapper mapper = new ObjectMapper();
        try {
            String json = mapper.writeValueAsString(data);
            return Response.ok(json, MediaType.APPLICATION_JSON).build();
        } catch (JsonProcessingException e) {
            return Response.serverError().build();
        }
    }
}
