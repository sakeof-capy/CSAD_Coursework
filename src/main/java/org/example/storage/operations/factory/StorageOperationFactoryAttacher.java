package org.example.storage.operations.factory;

import org.example.exceptions.CreationException;
import org.example.exceptions.HolderException;
import org.example.factories.OneParamFactory;
import org.example.storage.Storage;
import org.example.storage.operations.StorageOperation;
import org.example.utilities.holders.Holder;
import org.example.utilities.holders.StandardHolder;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

/**
 * StorageOperationFactoryAttacher - a class that is responsible for
 * creating the StorageOperationFactory objects. It encapsulates the
 * attaching the concrete operations to the factory that is to be created.
 * The only way to create a StorageOperationFactory is the following:
 * var factory = StorageOperationFactoryAttacher.factoryOf(storage);
 * The 'factory' will automatically be attached to the OperationType enum;
 * and be able to create operations of any types held OperationType.
 * It is vital to declare that the design of the factory is such that
 * the factory itself is independent on the operations it creates. So
 * the factory can be compiled out to the external lib, then linked
 * to some other project and be able to add new operation types easily.
 * I owe this trick to Andrei Alexandrescu from his "Modern C++ design" book
 */
public class StorageOperationFactoryAttacher {
    public static OneParamFactory<StorageOperation, OperationType> factoryOf(Storage storage) {
        var factory = new StorageOperationFactory(storage);
        attach(factory);
        return factory;
    }

    private static void attach(Holder<OperationType, Function<Storage, StorageOperation>> factory) {
        for(var operationType : OperationType.values()) {
            try {
                if(factory.holds(operationType)) continue;
                var constructor = operationType.getOperationConstructor();
                factory.hold(operationType, constructor);
            } catch (HolderException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static class StorageOperationFactory implements OneParamFactory<StorageOperation, OperationType>,
            Holder<OperationType, Function<Storage, StorageOperation>> {
        public StorageOperationFactory(Storage storage) {
            this.holder = new StandardHolder<>();
            this.storage = storage;
        }

        @Override
        public StorageOperation create(OperationType operationType) throws CreationException {
            var constructor = getHoldable(operationType)
                    .orElseThrow(() -> new CreationException("The operation type " + operationType.name() + " is not held."));
            return constructor.apply(storage);
        }

        @Override
        public void hold(OperationType operationType, Function<Storage, StorageOperation> constructor) throws HolderException {
            holder.hold(operationType, constructor);
        }

        @Override
        public void release(OperationType operationType) throws HolderException {
            holder.release(operationType);
        }

        @Override
        public Optional<Function<Storage, StorageOperation>> getHoldable(OperationType operationType) {
            return holder.getHoldable(operationType);
        }

        @Override
        public boolean holds(OperationType operationType) {
            return holder.holds(operationType);
        }

        @Override
        public Map<OperationType, Function<Storage, StorageOperation>> getMap() {
            return holder.getMap();
        }

        private final Holder<OperationType, Function<Storage, StorageOperation>> holder;
        private final Storage storage;
    }

}
