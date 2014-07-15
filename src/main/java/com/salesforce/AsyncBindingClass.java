/*
 * Copyright 2013, salesforce.com
 * All Rights Reserved
 * Company Confidential
 */

import java.util.Set;

import com.google.common.collect.Sets;

/**
 * This class generates a stub for the apex developer to use that exposes an asynchronous/continuation based API
 *
 */
class AsyncBindingClass extends BindingClass {

    AsyncBindingClass(Packages packages, APackage thisPackage, Binding binding, Definitions definitions, ApexTypeMapper typeMapper) throws ConnectionException, CalloutException {
        super(definitions, typeMapper, typeMapper.getSafeName("Async" + definitions.getService().getPort().getName()));
        this.packages = packages;
        loadStub(thisPackage, binding, CertOptions.SkipDeprecatedFields, OutputHttpHeadersOptions.SkipHeaderMap);
    }

    private Packages packages;
    private Set<ApexTypeName> createdFutures = Sets.newHashSet();
    
    @Override
    protected void addOperation(APackage pkg, Binding binding, Operation operation) throws ConnectionException, CalloutException {
        BeginAsyncOperationMethod begin = new BeginAsyncOperationMethod(definitions, typeMapper, operation, binding);
        methods.add(begin);
        
        /// add the future type if needed
        ApexTypeName futureType = begin.getReturnType();
        if (createdFutures.add(futureType)) {
            // Futures are created in a parallel package to their underlying type, this might be a different package to the current package
            // if the WSDL contains multiple schemas with different namespaces.
            String returnElementName = null;
            if ((!begin.isVoid()) && begin.shouldUnwrapOutParams()) {
                Element e = begin.getResponseElement();
                if (e != null)
                    returnElementName = typeMapper.getSafeName(e.getName());
            }
            ApexTypeName responseType = begin.getResponseType();
            CalloutFutureClass f = new CalloutFutureClass(definitions, typeMapper, operation, futureType.getJustTypeName(), responseType, begin.getRawReturnType(), returnElementName);
            if (futureType.hasPackageName())
                pkg = packages.getPackage(futureType.getPacakageName());
            pkg.addClass(f);
        }
    }
}
