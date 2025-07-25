package com.opyruso.coh.model;

import java.util.List;

import com.opyruso.coh.entity.Character;
import com.opyruso.coh.entity.DamageBuffType;
import com.opyruso.coh.entity.DamageType;
import com.opyruso.coh.entity.Picto;
import com.opyruso.coh.entity.Weapon;
import com.opyruso.coh.entity.Capacity;
import com.opyruso.coh.entity.CapacityType;
import com.opyruso.coh.entity.Outfit;

public class PublicData {
    public List<Character> characters;
    public List<DamageType> damageTypes;
    public List<DamageBuffType> damageBuffTypes;
    public List<Picto> pictos;
    public List<Weapon> weapons;
    public List<Capacity> capacities;
    public List<CapacityType> capacityTypes;
    public List<Outfit> outfits;
}
