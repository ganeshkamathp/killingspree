package com.sillygames.killingSpree.pool;

import java.util.ArrayList;

public abstract class Pool<T> {
        
        private int max;
        private int current;
        private ArrayList<T> objects; 
        
        public Pool() {
            current = 0;
            max = 32;
            objects = new ArrayList<T>();
            init();
        }
        
        public void setMax(int max) {
            this.max = max;
            init();
        }

        private void init() {
            objects.clear();
            for (int i=0; i < max; i++) {
                objects.add(getNewObject());
            }
        }

        abstract protected T getNewObject();

        public T obtain() {
            current = (current + 1) % max;
            T object = objects.get(current % max);
            if (object instanceof Poolable) {
                ((Poolable) object).reset();
            }
            return object;
        }
}
