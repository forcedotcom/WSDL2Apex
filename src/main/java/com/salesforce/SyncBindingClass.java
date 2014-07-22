package com.salesforce.ide.wsdl2apex.core;


/**
 * This generates the apex class that represents a soap binding (or Stub) that exposes a synchronous programming model
 *
 */
class SyncBindingClass extends BindingClass {
    
    SyncBindingClass(APackage pkg, Binding binding, Definitions definitions, ApexTypeMapper typeMapper) throws ConnectionException, CalloutException {
        super(definitions, typeMapper, typeMapper.getSafeName(definitions.getService().getPort().getName()));
        loadStub(pkg, binding, CertOptions.IncludeDeprecatedFields, OutputHttpHeadersOptions.IncludeHeaderMap);
    }
    
    @Override
    protected void addOperation(APackage pkg, Binding binding, Operation operation) throws ConnectionException, CalloutException {
        OperationMethod method = new OperationMethod(definitions, typeMapper, operation, binding);
        methods.add(method);
    }
}