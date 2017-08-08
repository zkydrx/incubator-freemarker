/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.freemarker.core;

import java.io.Serializable;

import org.apache.freemarker.core.model.TemplateModel;

/**
 * The type of a value differs from what was expected.
 */
public class UnexpectedTypeException extends TemplateException {
    
    public UnexpectedTypeException(Environment env, String description) {
        super(description, env);
    }

    UnexpectedTypeException(Environment env, _ErrorDescriptionBuilder description) {
        super(null, env, null, description);
    }

    UnexpectedTypeException(
            ASTExpression blamed, TemplateModel model, String expectedTypesDesc, Class[] expectedTypes, Environment env)
            throws InvalidReferenceException {
        super(null, env, blamed, newDescriptionBuilder(blamed, null, null, model, expectedTypesDesc, expectedTypes,
                env));
    }

    UnexpectedTypeException(
            ASTExpression blamed, TemplateModel model, String expectedTypesDesc, Class[] expectedTypes, String tip,
            Environment env)
            throws InvalidReferenceException {
        super(null, env, blamed, newDescriptionBuilder(blamed, null, null, model, expectedTypesDesc, expectedTypes,
                env)
                .tip(tip));
    }

    UnexpectedTypeException(
            ASTExpression blamed, TemplateModel model, String expectedTypesDesc, Class[] expectedTypes, Object[] tips,
            Environment env)
            throws InvalidReferenceException {
        super(null, env, blamed, newDescriptionBuilder(blamed, null, null, model, expectedTypesDesc, expectedTypes, env)
                .tips(tips));
    }

     /**
      * Used for assignments that use {@code +=} and such.
      */
    UnexpectedTypeException(
            String blamedAssignmentTargetVarName, TemplateModel model, String expectedTypesDesc, Class[] expectedTypes,
            Object[] tips,
            Environment env)
            throws InvalidReferenceException {
        super(null, env, null, newDescriptionBuilder(
                null, blamedAssignmentTargetVarName, null, model, expectedTypesDesc, expectedTypes, env).tips(tips));
    }

    /**
     * Used when the value of a directive/function argument has a different type than that the directive/function
     * expects.
     */
    UnexpectedTypeException(
            Serializable blamedArgumentNameOrIndex, TemplateModel model, String expectedTypesDesc, Class[] expectedTypes,
            Object[] tips,
            Environment env)
            throws InvalidReferenceException {
        super(null, env, null, newDescriptionBuilder(
                null, null, blamedArgumentNameOrIndex, model, expectedTypesDesc, expectedTypes, env).tips(tips));
    }

    private static _ErrorDescriptionBuilder newDescriptionBuilder(
            ASTExpression blamed, String blamedAssignmentTargetVarName, Serializable blamedArgumentNameOrIndex,
            TemplateModel model, String expectedTypesDesc, Class[] expectedTypes, Environment env)
            throws InvalidReferenceException {
        if (model == null) {
            throw InvalidReferenceException.getInstance(blamed, env);
        }

        _ErrorDescriptionBuilder errorDescBuilder = new _ErrorDescriptionBuilder(
                unexpectedTypeErrorDescription(expectedTypesDesc,
                        blamed, blamedAssignmentTargetVarName, blamedArgumentNameOrIndex,
                        model))
                .blame(blamed).showBlamer(true);
        if (model instanceof _UnexpectedTypeErrorExplainerTemplateModel) {
            Object[] tip = ((_UnexpectedTypeErrorExplainerTemplateModel) model).explainTypeError(expectedTypes);
            if (tip != null) {
                errorDescBuilder.tip(tip);
            }
        }
        return errorDescBuilder;
    }

    private static Object[] unexpectedTypeErrorDescription(
            String expectedTypesDesc,
            ASTExpression blamed, String blamedAssignmentTargetVarName, Serializable blamedArgumentNameOrIndex,
            TemplateModel model) {
        return new Object[] {
                "Expected ", new _DelayedAOrAn(expectedTypesDesc), ", but ", (
                        blamedAssignmentTargetVarName != null
                                ? new Object[] {
                                        "assignment target variable ",
                                        new _DelayedJQuote(blamedAssignmentTargetVarName) }
                        : blamedArgumentNameOrIndex != null
                                ? new Object[] {
                                        "the ",
                                        (blamedArgumentNameOrIndex instanceof Integer
                                                ? new _DelayedOrdinal(((Integer) blamedArgumentNameOrIndex) + 1)
                                                : new _DelayedJQuote(blamedArgumentNameOrIndex)),
                                        " argument"}
                        : blamed != null
                                ? "this"
                        : "the expression"
                ),
                " has evaluated to ",
                new _DelayedAOrAn(new _DelayedTemplateLanguageTypeDescription(model)),
                (blamedAssignmentTargetVarName == null ? ":" : ".")};
    }
    
}