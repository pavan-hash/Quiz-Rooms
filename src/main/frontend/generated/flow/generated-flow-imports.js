import { injectGlobalCss } from 'Frontend/generated/jar-resources/theme-util.js';

import { css, unsafeCSS, registerStyles } from '@vaadin/vaadin-themable-mixin';
import $cssFromFile_90 from 'Frontend/styles/login-view.css?inline';
import $cssFromFile_91 from 'Frontend/styles/home-view.css?inline';
import $cssFromFile_92 from 'Frontend/styles/home-layout.css?inline';
import $cssFromFile_93 from 'Frontend/styles/ai-quiz-generator.css?inline';
import $cssFromFile_94 from 'Frontend/styles/create-quiz-manually-view.css?inline';
import $cssFromFile_95 from 'Frontend/styles/register-view.css?inline';
import '@vaadin/vertical-layout/src/vaadin-vertical-layout.js';
import '@vaadin/login/src/vaadin-login-form.js';
import '@vaadin/horizontal-layout/src/vaadin-horizontal-layout.js';
import '@vaadin/app-layout/src/vaadin-app-layout.js';
import '@vaadin/app-layout/src/vaadin-drawer-toggle.js';
import '@vaadin/button/src/vaadin-button.js';
import '@vaadin/tooltip/src/vaadin-tooltip.js';
import 'Frontend/generated/jar-resources/disableOnClickFunctions.js';
import '@vaadin/text-field/src/vaadin-text-field.js';
import '@vaadin/select/src/vaadin-select.js';
import 'Frontend/generated/jar-resources/selectConnector.js';
import 'Frontend/generated/jar-resources/flow-component-renderer.js';
import 'Frontend/generated/jar-resources/flow-component-directive.js';
import 'lit';
import 'Frontend/generated/jar-resources/lit-renderer.ts';
import 'lit/directives/live.js';
import '@vaadin/integer-field/src/vaadin-integer-field.js';
import '@vaadin/progress-bar/src/vaadin-progress-bar.js';
import '@vaadin/notification/src/vaadin-notification.js';
import '@vaadin/dialog/src/vaadin-dialog.js';
import '@vaadin/combo-box/src/vaadin-combo-box.js';
import 'Frontend/generated/jar-resources/comboBoxConnector.js';
import '@vaadin/component-base/src/debounce.js';
import '@vaadin/component-base/src/async.js';
import '@vaadin/combo-box/src/vaadin-combo-box-placeholder.js';
import '@vaadin/multi-select-combo-box/src/vaadin-multi-select-combo-box.js';
import '@vaadin/grid/src/vaadin-grid.js';
import '@vaadin/grid/src/vaadin-grid-column.js';
import '@vaadin/grid/src/vaadin-grid-sorter.js';
import '@vaadin/checkbox/src/vaadin-checkbox.js';
import 'Frontend/generated/jar-resources/gridConnector.ts';
import '@vaadin/grid/src/vaadin-grid-active-item-mixin.js';
import 'Frontend/generated/jar-resources/vaadin-grid-flow-selection-column.js';
import '@vaadin/grid/src/vaadin-grid-column-group.js';
import '@vaadin/context-menu/src/vaadin-context-menu.js';
import 'Frontend/generated/jar-resources/contextMenuConnector.js';
import 'Frontend/generated/jar-resources/contextMenuTargetConnector.js';
import '@vaadin/component-base/src/gestures.js';
import '@vaadin/date-picker/src/vaadin-date-picker.js';
import 'Frontend/generated/jar-resources/datepickerConnector.js';
import 'date-fns/parse';
import '@vaadin/date-picker/src/vaadin-date-picker-helper.js';
import '@vaadin/email-field/src/vaadin-email-field.js';
import '@vaadin/password-field/src/vaadin-password-field.js';
import '@vaadin/form-layout/src/vaadin-form-layout.js';
import '@vaadin/form-layout/src/vaadin-form-item.js';
import '@vaadin/form-layout/src/vaadin-form-row.js';
import '@vaadin/common-frontend/ConnectionIndicator.js';
import 'Frontend/generated/jar-resources/ReactRouterOutletElement.tsx';
import 'react-router';
import 'react';

injectGlobalCss($cssFromFile_90.toString(), 'CSSImport end', document);

injectGlobalCss($cssFromFile_91.toString(), 'CSSImport end', document);

injectGlobalCss($cssFromFile_92.toString(), 'CSSImport end', document);

injectGlobalCss($cssFromFile_93.toString(), 'CSSImport end', document);

injectGlobalCss($cssFromFile_94.toString(), 'CSSImport end', document);

injectGlobalCss($cssFromFile_95.toString(), 'CSSImport end', document);

const loadOnDemand = (key) => {
  const pending = [];
  if (key === 'e04169528bc371598c4307768cd5560f823e08a878f796f077bc5c2bc789e12c') {
    pending.push(import('./chunks/chunk-1a83699bbfc401cdf1b0d78a2364f9c7a63baf2a6c8159f4b28e46401cc13b73.js'));
  }
  if (key === 'a5dc5f7033bc7813cf3f106697ec63bc1f47851a9a3221e1d8809b34561aa26c') {
    pending.push(import('./chunks/chunk-0675cc92f932bb5ea1b1a78a59c2880ac8771096709ed58a564bb6020fe26729.js'));
  }
  if (key === '2bec8324687ddd0994a23c191e4384ea49f19fa2977adc12f4f759f539cdccd3') {
    pending.push(import('./chunks/chunk-8937b6d1864c1d87224f5c9a111fa262ad982ad0da9752fc3e6880767020148e.js'));
  }
  if (key === '11d4bc75010748990ab1390dd309529d774f62901e5fdc746b541c27761ceb97') {
    pending.push(import('./chunks/chunk-3d1ebca8be7ae92dbca7fa6bce6b97dc7f1360c85e582eab7f4c48dd2e1a95d4.js'));
  }
  if (key === '63c250edcb947621a8a80e832f8cb3c9a9a1e3446bf18695b8b8533025d23bba') {
    pending.push(import('./chunks/chunk-c75ca8e4eaa1976784f0e806aba1bdd2b727bf050937637009e4facdfab80e7e.js'));
  }
  if (key === 'adfc6e11290f0bf698faab15a5be20bf3a17b6dd26f3032a4ddc34825eb2d935') {
    pending.push(import('./chunks/chunk-4c6deaa331717257d3f4d43834da58abb530fb6c462578399f4b6e77fefb1ae2.js'));
  }
  if (key === '1e0de681d502991d3f26e6ee05c504aa4b0763bf2106035c3b22e0514d73216e') {
    pending.push(import('./chunks/chunk-7e3f6881ff107661e93523f01044138f9221f7b1caeae95539589dfc818604b9.js'));
  }
  return Promise.all(pending);
}

window.Vaadin = window.Vaadin || {};
window.Vaadin.Flow = window.Vaadin.Flow || {};
window.Vaadin.Flow.loadOnDemand = loadOnDemand;
window.Vaadin.Flow.resetFocus = () => {
 let ae=document.activeElement;
 while(ae&&ae.shadowRoot) ae = ae.shadowRoot.activeElement;
 return !ae || ae.blur() || ae.focus() || true;
}