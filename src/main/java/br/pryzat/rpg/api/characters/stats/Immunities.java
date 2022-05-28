package br.pryzat.rpg.api.characters.stats;
/*
Classe responsavel pelo gerenciamento de imunidades dos jogadores.
Antes do plugin dar dano ou executar uma skill em algum jogador selecionado, será verificado se o alvo tem imunidade.
Por exemplo, se um Cavaleiro usa uma skill de puxar os inimigos proximos, caso um dos inimigos proximo esteja com a variavel
skills = true; ele não será puxado.
O nome dos metodos é assim de acordo com o contexto, seria pratico um metodo: checkSkillsImmunity(), mas considerando que a
classe se chama Immunities o metodo checkSkills() nesse contexto é sobre imunidade.
 */
public class Immunities {
    private boolean skills, damage;
    // No futuro, substuir o damage para dano fisico e dano magico, possibilitando imunidade a um dos dois, ou ao dois.
    public Immunities(){
        skills = false;
        damage = false;
    }

    /**
     * Define à imunidade do jogador para habilidades (skills)
     * @param order
     */
    public void setSkills(boolean order){
        this.skills = order;
    }

    /**
     * Define à imunidade ddo jogador para dano
     * @param order
     */
    public void setDamage(boolean order){
        this.damage = order;
    }

    /**
     * Verifica se o jogador tem imunidade (total) à habildiades (skills)
     * @return Uma boolean, se true a imunidade está ativada, se não está desativada.
     */
    public boolean checkSkills(){
        return skills;
    }

    /**
     * Verifica se o jogador tem imunidade (total) à dano (fisico e magico)
     * @return Uma boolean, se true a imunidade está ativada, se não está desativada.
     */
    public boolean checkDamage(){
        return damage;
    }
}
