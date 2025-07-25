package com.opyruso.coh.model;

import java.util.List;

import com.opyruso.coh.model.dto.CharacterWithDetails;
import com.opyruso.coh.model.dto.DamageTypeWithDetails;
import com.opyruso.coh.model.dto.DamageBuffTypeWithDetails;
import com.opyruso.coh.model.dto.PictoWithDetails;
import com.opyruso.coh.model.dto.WeaponWithDetails;
import com.opyruso.coh.model.dto.CapacityWithDetails;
import com.opyruso.coh.model.dto.CapacityTypeWithDetails;
import com.opyruso.coh.model.dto.OutfitWithDetails;

public class PublicData {
    public List<CharacterWithDetails> characters;
    public List<DamageTypeWithDetails> damageTypes;
    public List<DamageBuffTypeWithDetails> damageBuffTypes;
    public List<PictoWithDetails> pictos;
    public List<WeaponWithDetails> weapons;
    public List<CapacityWithDetails> capacities;
    public List<CapacityTypeWithDetails> capacityTypes;
    public List<OutfitWithDetails> outfits;
}
