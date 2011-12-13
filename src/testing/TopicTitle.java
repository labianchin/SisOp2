/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package testing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 *
 * @author luisarmando
 */
public class TopicTitle {

    public static class TitleTopicEntry<K, V> implements Entry<K, V> {

        private final K key;
        private V value;

        public TitleTopicEntry(final K key) {
            this.key = key;
        }

        public TitleTopicEntry(final K key, final V value) {
            this.key = key;
            this.value = value;
        }

        public K getKey() {
            return key;
        }

        public V getValue() {
            return value;
        }

        public V setValue(final V value) {
            final V oldValue = this.value;
            this.value = value;
            return oldValue;
        }
    }

    public static List<Entry<String, String>>  getTopicTitles() {
        List<Entry<String, String>> list = new ArrayList<Entry<String, String>>();
        list.add(new TitleTopicEntry<String, String>("Arranhas", "Arranhas atacam no vale"));
        list.add(new TitleTopicEntry<String, String>("Arranhas", "Arranhas e o ecossistema"));
        list.add(new TitleTopicEntry<String, String>("Arranhas", "Arranhas são seres vivos!!"));
        list.add(new TitleTopicEntry<String, String>("Cachorros", "Cachorros atacam pessoas"));
        list.add(new TitleTopicEntry<String, String>("Cachorros", "Cachorros mortos no vale"));
        list.add(new TitleTopicEntry<String, String>("Cachorros", "Cachorros são seres vivos!!"));
        list.add(new TitleTopicEntry<String, String>("Gatos", "Vão matar um gato na internet"));
        list.add(new TitleTopicEntry<String, String>("Gatos", "Gatos são seres vivos"));
        list.add(new TitleTopicEntry<String, String>("Provas Dacomp", "Alguém tem a prova de Teoria da Computação?"));
        list.add(new TitleTopicEntry<String, String>("Provas Dacomp", "Sumiram as provas"));
        list.add(new TitleTopicEntry<String, String>("Provas Dacomp", "Sem provas!!"));
        list.add(new TitleTopicEntry<String, String>("Gamefication", "Tornar o curso um jogo"));
        list.add(new TitleTopicEntry<String, String>("Gamefication", "As cadeiras são muito difíceis"));
        list.add(new TitleTopicEntry<String, String>("Gamefication", "Outras metodologias de avaliação"));
        return list;
    }
}
