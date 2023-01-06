/*
 * Copyright 2023 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uk.gov.hmrc.hmrcemailrenderer.templates.oss

import org.scalatest.{ EitherValues, Matchers, OptionValues, WordSpecLike }
import uk.gov.hmrc.hmrcemailrenderer.domain.MessagePriority
import uk.gov.hmrc.hmrcemailrenderer.templates.ServiceIdentifier.OSS
import uk.gov.hmrc.hmrcemailrenderer.templates.{ CommonParamsForSpec, TemplateLoader, TemplateLocator }

class OSSOverdueReturnsEmailConfirmationSpec
    extends WordSpecLike with Matchers with OptionValues with EitherValues with TemplateLoader
    with CommonParamsForSpec {

  "OSS Overdue Returns Email Confirmation" should {
    val templateLocator = new TemplateLocator {}
    val params = commonParameters ++ Map(
      "recipientName_line1" -> "Joe Bloggs",
      "businessName"        -> "Test Business",
      "period"              -> "1 July to 30 September 2021",
      "paymentDeadline"     -> "31 October 2021"
    )
    val template = templateLocator
      .templateGroups("OSS")
      .find(_.templateId == "oss_overdue_returns_email_confirmation")
      .get

    "render correct meta information" in {
      template.templateId shouldBe "oss_overdue_returns_email_confirmation"
      template.service shouldBe OSS
      template.fromAddress(Map.empty) shouldBe "VAT One Stop Shop Team <noreply@tax.service.gov.uk>"
      template.subject(Map.empty) shouldBe "HMRC: VAT return submitted"
      template.priority shouldBe Some(MessagePriority.Background)
    }

    "render correct html content" in {
      val htmlContent = template.htmlTemplate(params).toString

      htmlContent should include("You submitted a VAT return for the One Stop Shop Union scheme")
      htmlContent should include("Dear Joe Bloggs")
      htmlContent should include("HMRC has received your VAT return for 1 July to 30 September 2021.")
      htmlContent should include("Payment for this return was due by 31 October 2021.")
      htmlContent should include("You need to pay the return in full as soon as you can.")
      htmlContent should include(
        "If you submit late payments and returns, we may remove you from the scheme. " +
          "More information about One Stop Shop return deadlines is available on GOV.UK.")
      htmlContent should include("EU countries can charge you interest or penalties for late payments.")
      htmlContent should include("How to pay")
      htmlContent should include("Search GOV.UK for ‘Pay your OSS VAT Return’.")
      htmlContent should include("Sign into your Government Gateway account using your ID and password.")
      htmlContent should include("You can also pay without signing in using your VAT registration number.")
      htmlContent should include("From the VAT One Stop Shop team")
    }

    "render correct text content" in {
      val txtContent = template.plainTemplate(params).toString

      txtContent should include("You submitted a VAT return for the One Stop Shop Union scheme")
      txtContent should include("Dear Joe Bloggs")
      txtContent should include("HMRC has received your VAT return for 1 July to 30 September 2021.")
      txtContent should include("Payment for this return was due by 31 October 2021.")
      txtContent should include("You need to pay the return in full as soon as you can.")
      txtContent should include(
        "If you submit late payments and returns, we may remove you from the scheme. " +
          "More information about One Stop Shop return deadlines is available on GOV.UK.")
      txtContent should include("EU countries can charge you interest or penalties for late payments.")
      txtContent should include("How to pay")
      txtContent should include("Search GOV.UK for ‘Pay your OSS VAT Return’.")
      txtContent should include("Sign into your Government Gateway account using your ID and password.")
      txtContent should include("You can also pay without signing in using your VAT registration number.")
      txtContent should include("From the VAT One Stop Shop team")
    }
  }
}
