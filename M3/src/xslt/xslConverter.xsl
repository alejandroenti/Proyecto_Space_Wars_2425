<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
  
  <xsl:output method="html" encoding="UTF-8" indent="yes"/>
  
  <xsl:template match="/">
    <html>
      <head>
        <title>Battle Statistics</title>
      </head>
      <body>
        <h3>BATTLE STATISTICS</h3>

        <!-- tabla tropas -->
        <table border="1" cellpadding="5" cellspacing="0">
          <thead>
            <tr bgcolor="#ddd">
              <th>Army Planet</th>
              <th>Units</th>
              <th>Drops</th>
              <th>Initial Army Enemy</th>
              <th>Units</th>
              <th>Drops</th>
            </tr>
          </thead>
          <tbody>
            <xsl:for-each select="battle_summary/planet/troops/troop">
              <tr>
                <td><xsl:value-of select="name"/></td>
                <td><xsl:value-of select="units"/></td>
                <td><xsl:value-of select="drops"/></td>

                <xsl:variable name="pos" select="position()"/>
                <xsl:variable name="enemyTroop" select="/battle_summary/enemy/troops/troop[position()=$pos]"/>

                <td>
                  <xsl:choose>
                    <xsl:when test="$enemyTroop">
                      <xsl:value-of select="$enemyTroop/name"/>
                    </xsl:when>
                    <xsl:otherwise>-</xsl:otherwise>
                  </xsl:choose>
                </td>
                <td>
                  <xsl:choose>
                    <xsl:when test="$enemyTroop">
                      <xsl:value-of select="$enemyTroop/units"/>
                    </xsl:when>
                    <xsl:otherwise>-</xsl:otherwise>
                  </xsl:choose>
                </td>
                <td>
                  <xsl:choose>
                    <xsl:when test="$enemyTroop">
                      <xsl:value-of select="$enemyTroop/drops"/>
                    </xsl:when>
                    <xsl:otherwise>-</xsl:otherwise>
                  </xsl:choose>
                </td>
              </tr>
            </xsl:for-each>
          </tbody>
        </table>
        <br/>

        <!-- tabla coste -->
        <table border="1" cellpadding="5" cellspacing="0">
          <thead>
            <tr bgcolor="#ddd">
              <th colspan="2">Cost Army Planet</th>
              <th colspan="2">Cost Army Enemy</th>
            </tr>
          </thead>
          <tbody>
            <tr>
              <td>Metal:</td>
              <td><xsl:value-of select="battle_summary/planet/cost/metal"/></td>
              <td>Metal:</td>
              <td><xsl:value-of select="battle_summary/enemy/cost/metal"/></td>
            </tr>
            <tr>
              <td>Deuterium:</td>
              <td><xsl:value-of select="battle_summary/planet/cost/deuterium"/></td>
              <td>Deuterium:</td>
              <td><xsl:value-of select="battle_summary/enemy/cost/deuterium"/></td>
            </tr>
          </tbody>
        </table>
        <br/>

        <!-- tabla perdidas -->
        <table border="1" cellpadding="5" cellspacing="0">
          <thead>
            <tr bgcolor="#ddd">
              <th colspan="2">Losses Army Planet</th>
              <th colspan="2">Losses Army Enemy</th>
            </tr>
          </thead>
          <tbody>
            <tr>
              <td>Metal:</td>
              <td><xsl:value-of select="battle_summary/planet/losses/metal"/></td>
              <td>Metal:</td>
              <td><xsl:value-of select="battle_summary/enemy/losses/metal"/></td>
            </tr>
            <tr>
              <td>Deuterium:</td>
              <td><xsl:value-of select="battle_summary/planet/losses/deuterium"/></td>
              <td>Deuterium:</td>
              <td><xsl:value-of select="battle_summary/enemy/losses/deuterium"/></td>
            </tr>
            <tr>
              <td>Weighed:</td>
              <td><xsl:value-of select="battle_summary/planet/losses/weighed"/></td>
              <td>Weighed:</td>
              <td><xsl:value-of select="battle_summary/enemy/losses/weighed"/></td>
            </tr>
          </tbody>
        </table>
        <br/>

        <!-- tabla residuos -->
        <table border="1" cellpadding="5" cellspacing="0">
          <thead>
            <tr bgcolor="#ddd"><th colspan="2">Waste Generated</th></tr>
          </thead>
          <tbody>
            <tr>
              <td>Metal:</td>
              <td><xsl:value-of select="battle_summary/generated_waste/metal"/></td>
            </tr>
            <tr>
              <td>Deuterium:</td>
              <td><xsl:value-of select="battle_summary/generated_waste/deuterium"/></td>
            </tr>
          </tbody>
        </table>
        <br/>

        <!-- ganador zzz -->
        <table border="1" cellpadding="5" cellspacing="0">
          <tr bgcolor="#ddd">
            <th>Winner</th>
          </tr>
          <tr>
            <td><xsl:value-of select="battle_summary/winner"/></td>
          </tr>
        </table>

      </body>
    </html>
  </xsl:template>
</xsl:stylesheet>