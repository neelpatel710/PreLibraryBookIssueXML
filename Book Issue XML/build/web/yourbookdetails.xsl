<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
<xsl:output method="html" indent="yes"/>
<xsl:param name="user"/>
    <xsl:template match="/">
        <html>
        <head>
            <link rel="stylesheet" href="table.css"/>
        </head>
        <body>
        <table class="table_your" cellpadding="20">
            <tr>
                <th width="200" >Unique ID</th>
                <th>Book Details</th>
            </tr>
            <xsl:variable name="query" select="/issueBooks/book[userName=$user]"/>
            <xsl:for-each select="$query">
                <tr>
                    <td>
                        <b><xsl:value-of select="@uid"/></b>
                   </td>
                   <td>
                       Department: <b><xsl:value-of select="dept"/></b><br></br>
                       Book: <b><xsl:value-of select="bookName"/></b><br></br>
                       Publisher: <b><xsl:value-of select="bookPub"/></b><br></br>
                       Collect Before: <b><xsl:value-of select="day_r"/>-<xsl:value-of select="month_r"/>-<xsl:value-of select="year_r"/></b>
                   </td>
                </tr>
            </xsl:for-each>
        </table>
        </body>
        </html>
    </xsl:template>

</xsl:stylesheet>
