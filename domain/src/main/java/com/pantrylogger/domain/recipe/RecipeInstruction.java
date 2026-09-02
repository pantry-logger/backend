package com.pantrylogger.domain.recipe;

import java.util.UUID;

public class RecipeInstruction {

    private RecipeInstructionUUID uuid;
    private String instruction;

    public record RecipeInstructionUUID(UUID uuid) {
        public RecipeInstructionUUID(String strUUID) {
            this(UUID.fromString(strUUID));
        }
    }

    public RecipeInstruction(RecipeInstructionUUID uuid, String instruction) {
        this.uuid = uuid;
        this.instruction = instruction;
    }

    public RecipeInstructionUUID getUuid() {
        return uuid;
    }

    public void setUuid(RecipeInstructionUUID uuid) {
        this.uuid = uuid;
    }

    public String getInstruction() {
        return instruction;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }
}